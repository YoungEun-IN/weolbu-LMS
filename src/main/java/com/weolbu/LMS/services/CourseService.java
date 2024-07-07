package com.weolbu.LMS.services;

import com.weolbu.LMS.dtos.CourseRequest;
import com.weolbu.LMS.dtos.CourseResponse;
import com.weolbu.LMS.entities.Course;
import com.weolbu.LMS.entities.Registration;
import com.weolbu.LMS.exceptions.DataNotFoundException;
import com.weolbu.LMS.repositories.CourseRepository;
import com.weolbu.LMS.repositories.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;

    @Transactional
    public void create(CourseRequest courseRequest) {
        courseRepository.save(buildCourse(courseRequest));
    }

    private static Course buildCourse(CourseRequest courseRequest) {
        return Course.builder()
                .name(courseRequest.getName())
                .maxEnrollment(courseRequest.getMaxEnrollment())
                .price(courseRequest.getPrice())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> getList(Pageable pageable) {
        return courseRepository.findAllBy(pageable);

    }

    @Transactional
    public void enroll(String email, List<Long> courseIdList) {
        List<Registration> registrationList = new ArrayList<>();
        for (Course course : getCourses(courseIdList)) {
            validateMaxEnrollment(course);
            registrationList.add(buildRegistration(email, course));
        }
        registrationRepository.saveAll(registrationList);
    }

    private static Registration buildRegistration(String email, Course course) {
        return Registration.builder()
                .email(email)
                .course(course)
                .build();
    }

    private void validateMaxEnrollment(Course course) {
        Long count = registrationRepository.countByCourseId(course.getId());
        if (count >= course.getMaxEnrollment()) {
            throw new IllegalStateException("개수 초과");
        }
    }

    private List<Course> getCourses(List<Long> courseIdList) {
        return courseIdList.stream()
                .map(courseId -> courseRepository.findById(courseId).orElseThrow(() -> new DataNotFoundException("강의가 존재하지 않습니다.")))
                .toList();
    }
}
