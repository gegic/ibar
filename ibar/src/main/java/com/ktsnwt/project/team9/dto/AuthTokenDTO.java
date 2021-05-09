package com.ktsnwt.project.team9.dto;

import com.ktsnwt.project.team9.model.Authority;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthTokenDTO {

 private String accessToken;
 private Long expiresIn;
 private List<Authority> authorities;

}
