package com.weolbu.LMS.services;

import com.weolbu.LMS.dtos.SignupRequest;
import com.weolbu.LMS.entities.Role;
import com.weolbu.LMS.entities.Member;
import com.weolbu.LMS.exceptions.DataNotFoundException;
import com.weolbu.LMS.repositories.MemberRepository;
import com.weolbu.LMS.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void create(SignupRequest signupRequest) {
        validateEmail(signupRequest.getEmail());

        memberRepository.save(getMember(signupRequest));
    }

    private Member getMember(SignupRequest signupRequest) {
        return Member.builder()
                .email(signupRequest.getEmail())
                .roles(Set.of(getRole(signupRequest)))
                .name(signupRequest.getName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();
    }

    private Role getRole(SignupRequest signupRequest) {
        return roleRepository.findByMemberType(signupRequest.getMemberType())
                .orElseThrow(() -> new DataNotFoundException("Role을 찾을 수 없습니다."));
    }

    public void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복 이메일 입니다.");
        }
    }
}