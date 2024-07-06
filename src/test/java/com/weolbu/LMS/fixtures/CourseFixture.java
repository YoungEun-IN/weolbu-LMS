package com.weolbu.LMS.fixtures;

import com.weolbu.LMS.dtos.CourseResponse;
import com.weolbu.LMS.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class CourseFixture {
    public static Course createCourse() {
        return Course.builder()
                .id(1L)
                .name("강의1")
                .maxEnrollment(5L)
                .build();
    }

    public static Page<CourseResponse> createCourseResponsePage(Pageable pageable) {
        return new PageImpl<>(List.of(CourseResponse.builder()
                        .price(100L)
                        .name("강의1")
                        .maxEnrollment(10L)
                        .registrationCount(3L)
                        .registrationRate(0.3)
                        .createdDateTime(LocalDateTime.of(2024, Month.APRIL, 11, 10, 11))
                        .build(),
                CourseResponse.builder()
                        .price(10L)
                        .name("강의2")
                        .maxEnrollment(10L)
                        .registrationCount(3L)
                        .registrationRate(0.3)
                        .createdDateTime(LocalDateTime.of(2024, Month.APRIL, 11, 11, 11))
                        .build()), pageable, 2);
    }
}
