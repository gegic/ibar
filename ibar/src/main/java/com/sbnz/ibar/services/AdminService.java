package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.UserDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.mapper.UserMapper;
import com.sbnz.ibar.model.Admin;
import com.sbnz.ibar.model.Authority;
import com.sbnz.ibar.model.User;
import com.sbnz.ibar.repositories.AdminRepository;
import com.sbnz.ibar.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final UserRepository userRepository;

    private final AuthorityService authorityService;

    private final PasswordEncoder passwordEncoder;

    private final GenerateRandomPasswordService grpService;

    private final MailService mailService;

    private final UserMapper userMapper;

    @Transactional
    public Iterable<UserDto> getAll() {
        List<Admin> admins = adminRepository.findAll();

        return admins.stream().map(this::toAdminDto).collect(Collectors.toList());
    }

    @Transactional
    public UserDto create(UserDto entity) throws EntityAlreadyExistsException {
        Optional<User> user = userRepository.findByEmail(entity.getEmail());

        if (user.isPresent()) {
            throw new EntityAlreadyExistsException(entity.getEmail(), user.get().getId());
        }

        Admin admin = new Admin(entity);

        admin.setId(UUID.randomUUID());

        List<Authority> auth = authorityService.findByName("ROLE_ADMIN");

        admin.setAuthorities(auth);

        String newPassword = grpService.generateRandomPassword();
        admin.setPassword(passwordEncoder.encode(newPassword));

        admin.setLastPasswordResetDate(new Date().getTime());

        mailService.sendMail(admin.getEmail(), "Account activation", "You are now new administrator of IBAR. Congratulations!\n Your credentials are: \n\tEmail: " + admin.getEmail() +
                "\n\tPassoword: " + newPassword);

        admin = adminRepository.save(admin);

        return this.toAdminDto(admin);
    }

    @Transactional
    public boolean delete(UUID id) {
        Optional<Admin> admin = adminRepository.findById(id);

        if (admin.isEmpty()) {
            return false;
        }

        adminRepository.deleteById(id);

        return true;
    }

    private UserDto toAdminDto(Admin admin) {
        return userMapper.toUserDto(admin);
    }

}
