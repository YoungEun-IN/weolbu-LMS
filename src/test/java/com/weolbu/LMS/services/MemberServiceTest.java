package com.weolbu.LMS.services;

import com.weolbu.LMS.dtos.SignupRequest;
import com.weolbu.LMS.entities.Member;
import com.weolbu.LMS.entities.Role;
import com.weolbu.LMS.enums.MemberType;
import com.weolbu.LMS.exceptions.DataNotFoundException;
import com.weolbu.LMS.repositories.MemberRepository;
import com.weolbu.LMS.repositories.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 생성 성공 테스트")
    void create_Success() {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setName("Test User");
        signupRequest.setPassword("password");
        signupRequest.setMemberType(MemberType.STUDENT);

        Role role = new Role();
        role.setMemberType(MemberType.STUDENT);

        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByMemberType(any(MemberType.class))).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        memberService.create(signupRequest);

        // Then
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("중복 이메일로 인한 회원 생성 실패 테스트")
    void create_Fail_DuplicateEmail() {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");

        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.create(signupRequest);
        });

        assertEquals("중복 이메일 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 Role로 인한 회원 생성 실패 테스트")
    void create_Fail_RoleNotFound() {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setMemberType(MemberType.STUDENT);

        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByMemberType(any(MemberType.class))).thenReturn(Optional.empty());

        // When & Then
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            memberService.create(signupRequest);
        });

        assertEquals("Role을 찾을 수 없습니다.", exception.getMessage());
    }
}