package com.weolbu.LMS.fixtures;

import com.weolbu.LMS.dtos.CourseResponse;
import com.weolbu.LMS.entities.Course;
import com.weolbu.LMS.entities.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class CourseFixture {
    public static List<Course> createCourseList(int size) {
        List<Course> courseList = new ArrayList<>();
        for (long i = 1L; i <= size; i++) {
            courseList.add(createCourse(i));
        }
        return courseList;
    }

    public static Course createCourse() {
        return createCourse(1L);
    }

    public static Course createCourse(Long courseId) {
        return Course.builder()
                .id(courseId)
                .price(10000L)
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

    public static List<Registration> createRegistrationList(List<Course> courseList) {
        List<Registration> registrationList = new ArrayList<>();
        for (Course course:courseList) {
            Registration registration = Registration.builder()
                    .course(course)
                    .build();
            registrationList.add(registration);
        }

        return registrationList;
    }

    public static Registration createRegistration(Long registrationId, Course course) {
        return Registration.builder()
                .id(registrationId)
                .course(course)
                .build();
    }
}
