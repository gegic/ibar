package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.AuthTokenDto;
import com.sbnz.ibar.dto.UserLoginDto;
import com.sbnz.ibar.exceptions.EmailTemporarilyBlockedException;
import com.sbnz.ibar.exceptions.IpTemporarilyBlockedException;
import com.sbnz.ibar.model.User;
import com.sbnz.ibar.repositories.UserRepository;
import com.sbnz.ibar.rto.EmailCheckFact;
import com.sbnz.ibar.rto.IpCheckFact;
import com.sbnz.ibar.rto.events.LoginEvent;
import com.sbnz.ibar.security.TokenUtils;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final TokenUtils tokenUtils;
    private final AuthenticationManager authenticationManager;
    private final KieService kieService;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

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
            LoginEvent loginEvent =
                    new LoginEvent(new Date(), present, ipAddress, false);
            loginSession.insert(loginEvent);
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

}
