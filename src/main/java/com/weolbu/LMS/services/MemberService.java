package com.weolbu.LMS.services;

import com.weolbu.LMS.dtos.SignupRequest;
import com.weolbu.LMS.entities.Member;
import com.weolbu.LMS.repositories.MemberRepository;
import com.weolbu.LMS.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(SignupRequest signupRequest) {
        return memberRepository.save(signupRequest.toEntity(passwordEncoder));
    }
}