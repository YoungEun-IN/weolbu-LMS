package com.weolbu.LMS.dtos;

import com.weolbu.LMS.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "사용자 이메일은 필수 입력 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$|^(?=.*[a-z])(?=.*[A-Z])[a-zA-Z]{6,10}$"
            ,message = "비밀번호는 최소 6자 이상 10자 이하 & 영문 소문자, 대문자, 숫자 중 최소 두 가지 이상 조합 필요")
    private String password;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "휴대폰번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^(01[016789])-?\\d{3,4}-?\\d{4}$")
    private String phoneNumber;

    private MemberType memberType;
}
