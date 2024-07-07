package com.weolbu.LMS.services;

import com.weolbu.LMS.entities.Member;
import com.weolbu.LMS.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public User loadUserByUsername(final String email) {
        return memberRepository.findOneWithRolesByEmail(email)
                .map(this::convertToUser)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 사용자를 찾을 수 없습니다."));
    }

    private User convertToUser(Member member) {
        List<GrantedAuthority> grantedAuthorities = member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getMemberType()))
                .collect(Collectors.toList());
        return new User(member.getEmail(),
                member.getPassword(),
                grantedAuthorities);
    }
}
