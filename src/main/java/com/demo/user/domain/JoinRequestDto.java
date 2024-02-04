package com.demo.user.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinRequestDto {
    @NotBlank(message = "아이디는 필수 입니다.")
    private String userId;
    @NotBlank(message = "이름은 필수 입니다.")
    private String userName;
    @NotBlank(message = "패스워드는 필수 입니다.")
    private String password;
}
