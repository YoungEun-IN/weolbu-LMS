package com.weolbu.LMS.controllers;

import com.weolbu.LMS.configurations.jwt.JwtFilter;
import com.weolbu.LMS.configurations.jwt.TokenProvider;
import com.weolbu.LMS.dtos.LoginRequest;
import com.weolbu.LMS.dtos.SignupRequest;
import com.weolbu.LMS.dtos.TokenDto;
import com.weolbu.LMS.entities.Member;
import com.weolbu.LMS.exceptions.ExpiredRefreshTokenException;
import com.weolbu.LMS.services.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Member"})
@Slf4j
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;

    @Value("${jwt.access-token-validity-in-seconds}")
    long accessTokenValidityInSeconds;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    long refreshTokenValidityInSecond;


    @ApiOperation(value = "회원 생성", notes = "회원을 생성한다.")
    @PostMapping("")
    public ResponseEntity<String> create(@ApiParam(value = "회원", required = true) @RequestBody @Valid SignupRequest signupRequest) {
        Member member = memberService.create(signupRequest);
        return new ResponseEntity<>("회원 가입 완료되었습니다.", HttpStatus.CREATED);
    }

    @ApiOperation(value = "이메일 중복 확인", notes = "이메일 중복을 확인한다.")
    @GetMapping("/validate")
    public ResponseEntity<String> create(@ApiParam(value = "이메일", required = true) String email) {
        memberService.validateEmail(email);
        return new ResponseEntity<>("사용 가능한 이메일입니다.", HttpStatus.OK);
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createToken(authentication, accessTokenValidityInSeconds);
        String refreshToken = tokenProvider.createToken(authentication, refreshTokenValidityInSecond);

        return new ResponseEntity<>(new TokenDto(accessToken, refreshToken), HttpStatus.OK);
    }

    @ApiOperation(value = "리프레쉬토큰으로 액세스토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> authorize(HttpServletRequest request) {
        log.debug("리프레쉬 토큰 발급요청");
        String refreshToken = JwtFilter.resolveToken(request);

        if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
            Authentication authentication = tokenProvider.getAuthentication(refreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = tokenProvider.createToken(authentication, accessTokenValidityInSeconds);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다.", authentication.getName());
            return new ResponseEntity<>(new TokenDto(accessToken, refreshToken), HttpStatus.OK);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다");
            throw new ExpiredRefreshTokenException();
        }
    }
}
