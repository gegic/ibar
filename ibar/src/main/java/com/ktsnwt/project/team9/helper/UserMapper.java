package com.ktsnwt.project.team9.helper;

import com.ktsnwt.project.team9.dto.UserDTO;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.model.Reader;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.repositories.IAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    private final IAuthorityRepository authorityRepository;
    
    @Autowired
    public UserMapper(IAuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }
    
    public User toEntity(UserDTO dto) {
        User entity;
        switch (dto.getUserType()) {
            case ADMIN:
                entity = new Admin();
                break;
            case READER:
                entity = new Reader();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dto.getUserType());
        }

        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPassword(dto.getPassword());
        entity.setEnabled(dto.isEnabled());
        entity.setAuthorities(dto.getAuthorities().stream()
                .map(au -> authorityRepository.getOne(au.getId())).collect(Collectors.toList()));
        return entity;
    }
}
