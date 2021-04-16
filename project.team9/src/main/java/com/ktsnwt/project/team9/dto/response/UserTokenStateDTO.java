package com.ktsnwt.project.team9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTokenStateDTO {

 private String accessToken;
 private Long expiresIn;

}
