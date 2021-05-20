package com.sbnz.ibar.dto;

import com.sbnz.ibar.model.Authority;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthTokenDto {

 private String accessToken;
 private Long expiresIn;
 private List<Authority> authorities;

}
