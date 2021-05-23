package com.sbnz.ibar.dto;

import com.sbnz.ibar.model.Authority;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthTokenDto {
    private long userId;
    private String accessToken;
    private List<Authority> authorities;
    private String userInitials;
}
