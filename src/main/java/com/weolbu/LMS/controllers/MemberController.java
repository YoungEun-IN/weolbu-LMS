package com.weolbu.LMS.controllers;

import com.weolbu.LMS.dtos.SignupRequest;
import com.weolbu.LMS.entities.Member;
import com.weolbu.LMS.services.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Member"})
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "회원 생성", notes = "회원을 생성한다.")
    @PostMapping("")
    public ResponseEntity<String> create(@ApiParam(value = "회원", required = true) @RequestBody @Valid SignupRequest signupRequest) {
        Member member = memberService.create(signupRequest);
        return new ResponseEntity<>("email : " + member.getEmail(), HttpStatus.CREATED);
    }
}
