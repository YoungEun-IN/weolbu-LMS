package com.weolbu.LMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weolbu.LMS.dtos.SignupRequest;
import com.weolbu.LMS.enums.MemberType;
import com.weolbu.LMS.exceptions.ExpiredRefreshTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    MemberController memberController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest httpServletRequest;

    @DisplayName("예외 테스트: 생성 요청 시 비어있는 이름이 전달되면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithNullAndEmptyNameTest(String name) throws Exception {
        SignupRequest signupRequest = SignupRequest.builder()
                .email("o0myself0o@naver.com")
                .name(name)
                .memberType(MemberType.STUDENT)
                .phoneNumber("01028763463")
                .password("Abcd1234G")
                .build();

        String requestAsString = objectMapper.writeValueAsString(signupRequest);

        this.mockMvc.perform(post("/members")
                        .content(requestAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @Test
    @DisplayName("회원 생성 테스트")
    public void testCreateMember1() {
        SignupRequest signupRequest = SignupRequest.builder()
                .email("o0myself0o@naver.com")
                .name("인영은")
                .memberType(MemberType.STUDENT)
                .phoneNumber("01028763463")
                .password("Abcd1234G")
                .build();

        ResponseEntity<String> response = memberController.create(signupRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("회원 가입 완료되었습니다.", response.getBody());
    }

    @Test
    @DisplayName("회원 생성 실패 테스트")
    public void testCreateMember2() {
        SignupRequest signupRequest = SignupRequest.builder()
                .email("o0myself0o@naver.com")
                .name("인영은")
                .memberType(MemberType.STUDENT)
                .phoneNumber("01028763463")
                .build();

        assertThrows(IllegalArgumentException.class,() -> memberController.create(signupRequest));
    }

    @Test
    @DisplayName("이메일 중복 확인 테스트")
    public void testValidateEmail() {
        String email = "test@example.com";

        ResponseEntity<String> response = memberController.create(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("사용 가능한 이메일입니다.", response.getBody());
    }

    @Test
    @DisplayName("유효하지 않은 리프레쉬 토큰으로 액세스 토큰 재발급 시도 테스트")
    public void testAuthorizeWithInvalidToken() {
        String refreshToken = "invalid-refresh-token";

        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer " + refreshToken);

        assertThrows(ExpiredRefreshTokenException.class, () -> {
            memberController.authorize(httpServletRequest);
        });
    }
}