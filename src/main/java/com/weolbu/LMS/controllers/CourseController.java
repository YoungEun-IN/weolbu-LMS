package com.weolbu.LMS.controllers;

import com.weolbu.LMS.dtos.CourseDto;
import com.weolbu.LMS.services.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @ApiOperation(value = "강의 생성")
    @PostMapping("")
    public ResponseEntity<String> create(@ApiParam(value = "강의", required = true) @RequestBody @Valid CourseDto courseDto) {
        courseService.create(courseDto);
        return new ResponseEntity<>("강의가 생성되었습니다.", HttpStatus.CREATED);
    }

    @ApiOperation(value = "강의목록조회")
    @GetMapping("")
    public List<CourseDto> getList(Pageable pageable) {
        return courseService.getList(pageable);
    }

    @ApiOperation(value = "수강신청")
    @GetMapping("/enroll")
    public void enroll(@AuthenticationPrincipal String email, List<Long> courseIdList) {
        courseService.enroll(email, courseIdList);
    }
}
