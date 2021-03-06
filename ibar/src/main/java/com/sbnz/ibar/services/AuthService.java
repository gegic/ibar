package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.AuthTokenDto;
import com.sbnz.ibar.dto.UserDto;
import com.sbnz.ibar.dto.UserLoginDto;
import com.sbnz.ibar.exceptions.EmailTemporarilyBlockedException;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.exceptions.IpTemporarilyBlockedException;
import com.sbnz.ibar.mapper.UserMapper;
import com.sbnz.ibar.model.Authority;
import com.sbnz.ibar.model.Rank;
import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.model.User;
import com.sbnz.ibar.repositories.RankRepository;
import com.sbnz.ibar.repositories.ReaderRepository;
import com.sbnz.ibar.repositories.UserRepository;
import com.sbnz.ibar.rto.EmailCheckFact;
import com.sbnz.ibar.rto.IpCheckFact;
import com.sbnz.ibar.rto.events.OnLoggedIn;
import com.sbnz.ibar.security.TokenUtils;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.InvalidAttributeValueException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final TokenUtils tokenUtils;
    private final AuthenticationManager authenticationManager;
    private final KieService kieService;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    private final ReaderRepository readerRepository;

    private final AuthorityService authorityService;

    private final PasswordEncoder passwordEncoder;

    private final GenerateRandomPasswordService grpService;

    private final MailService mailService;

    private final UserMapper userMapper;
    private final RankRepository rankRepository;


    public AuthTokenDto login(UserLoginDto loginDto) {
        KieSession loginSession = kieService.getLoginSession();
        String ipAddress = this.request.getHeader("X-Forward-For") != null ?
                this.request.getHeader("X-Forward-For") : this.request.getRemoteAddr();
        User user;

        IpCheckFact ipCheck = new IpCheckFact(ipAddress, true);
        loginSession.insert(ipCheck);
        loginSession.fireAllRules();

        if (!ipCheck.isAllowed()) {
            throw new IpTemporarilyBlockedException(
                    String.format("IP address %s was temporarily blocked", ipAddress));
        }

        EmailCheckFact emailCheck = new EmailCheckFact(loginDto.getEmail(), true);
        loginSession.insert(emailCheck);
        loginSession.fireAllRules();

        if (!emailCheck.isAllowed()) {
            throw new EmailTemporarilyBlockedException(
                    String.format("Account for %s was temporarily blocked", loginDto.getEmail()));
        }

        try {
            user = (User) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword())).getPrincipal();
        } catch (AuthenticationException e) {
            Optional<User> found = userRepository.findByEmail(loginDto.getEmail());
            User present = found.orElse(null);
            OnLoggedIn onLoggedIn =
                    new OnLoggedIn(new Date(), present, ipAddress, false);
            loginSession.insert(onLoggedIn);
            loginSession.fireAllRules();
            throw e;
        }

        return new AuthTokenDto(
                user.getId(),
                this.tokenUtils.generateToken(user.getEmail()),
                user.getAuthorities(),
                user.getInitials()
        );
    }

    public UserDto registerNewUser(UserDto entity) throws EntityAlreadyExistsException, EntityDoesNotExistException {
        Optional<User> user = userRepository.findByEmail(entity.getEmail());

        if (user.isPresent()) {
            throw new EntityAlreadyExistsException(entity.getEmail(), user.get().getId());
        }

        Reader reader = new Reader(entity);

        reader.setId(UUID.randomUUID());

        List<Authority> auth = authorityService.findByName("ROLE_READER");

        reader.setAuthorities(auth);

        String newPassword = grpService.generateRandomPassword();
        reader.setPassword(passwordEncoder.encode(newPassword));

        Rank rank = rankRepository.findByPoints(0).orElseThrow(() -> new EntityDoesNotExistException(Rank.class.getName(), "points"));
        reader.setRank(rank);
        reader.setLastPasswordResetDate(new Date().getTime());

        mailService.sendMail(reader.getEmail(), "Account activation", "You are now new user of IBAR. Congratulations!\n Your credentials are: \n\tEmail: " + reader.getEmail() +
                "\n\tPassoword: " + newPassword);

        reader = readerRepository.save(reader);

        kieService.getClassifySession().insert(reader);
        kieService.getClassifySession().fireAllRules();

        return this.toReaderDto(reader);
    }

    @Transactional
    public void changePassword(String oldPassword, String newPassword) throws IllegalAccessException, InvalidAttributeValueException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        String email;

        try {
            email = ((User) currentUser.getPrincipal()).getEmail();
        } catch (Exception e) {
            throw new IllegalAccessException("Invalid token.");
        }

        User user = userRepository.findByEmail(email).orElse(null);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, oldPassword));

        assert user != null;
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

        userRepository.save(user);
    }

    private UserDto toReaderDto(Reader reader) {
        return userMapper.toUserDto(reader);
    }

}
