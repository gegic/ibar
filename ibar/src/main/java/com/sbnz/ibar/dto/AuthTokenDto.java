package com.sbnz.ibar.dto;

import com.sbnz.ibar.model.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthTokenDto {
    private UUID userId;
    private String accessToken;
    private List<Authority> authorities;
    private String userInitials;
}
