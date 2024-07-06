package com.weolbu.LMS.services;

import com.weolbu.LMS.dtos.CourseDto;
import com.weolbu.LMS.entities.Course;
import com.weolbu.LMS.entities.Member;
import com.weolbu.LMS.entities.Registration;
import com.weolbu.LMS.exceptions.DataNotFoundException;
import com.weolbu.LMS.repositories.CourseRepository;
import com.weolbu.LMS.repositories.MemberRepository;
import com.weolbu.LMS.repositories.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;

    @Transactional
    public void create(CourseDto courseDto) {
        Course course = Course.builder()
                .name(courseDto.getName())
                .maxEnrollment(courseDto.getMaxEnrollment())
                .price(courseDto.getPrice())
                .build();

        courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getList(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .stream().map(CourseDto::fromEntity)
                .toList();

    }

    @Transactional
    public void enroll(String email, List<Long> courseIdList) {
        List<Registration> registrationList = new ArrayList<>();
        for (Course course : getCourses(courseIdList)) {
            Registration registration = Registration.builder()
                    .member(getMember(email))
                    .course(course)
                    .build();
            registrationList.add(registration);
        }
        registrationRepository.saveAll(registrationList);
    }

    private Member getMember(String email) {
        return memberRepository.findById(email).orElseThrow(() -> new DataNotFoundException("사용자가 존재하지 않습니다."));
    }

    private List<Course> getCourses(List<Long> courseIdList) {
        return courseIdList.stream()
                .map(courseId -> courseRepository.findById(courseId).orElseThrow(() -> new DataNotFoundException("강의가 존재하지 않습니다.")))
                .toList();
    }
}
