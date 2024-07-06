package com.weolbu.LMS.controllers;

import com.weolbu.LMS.dtos.CourseRequest;
import com.weolbu.LMS.dtos.CourseResponse;
import com.weolbu.LMS.facade.RedissonLockCourseFacade;
import com.weolbu.LMS.services.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Course"})
@Slf4j
@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final RedissonLockCourseFacade redissonLockCourseFacade;

    @ApiOperation(value = "강의 생성")
    @Secured("ROLE_LECTURER")
    @PostMapping("")
    public ResponseEntity<String> create(@ApiParam(value = "강의", required = true) @RequestBody @Valid CourseRequest courseRequest) {
        courseService.create(courseRequest);
        return new ResponseEntity<>("강의가 생성되었습니다.", HttpStatus.CREATED);
    }

    @ApiOperation(value = "강의목록조회")
    @GetMapping("")
    public Page<CourseResponse> getList(Pageable pageable) {
        return courseService.getList(pageable);
    }

    @ApiOperation(value = "수강신청")
    @Secured({"ROLE_LECTURER", "ROLE_STUDENT"})
    @PostMapping("/enroll")
    public void enroll(@AuthenticationPrincipal User user, @RequestBody List<Long> courseIdList) {
        redissonLockCourseFacade.enroll(user.getUsername(), courseIdList);
    }
}
