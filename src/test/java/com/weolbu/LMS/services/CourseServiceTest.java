package com.weolbu.LMS.services;

import com.weolbu.LMS.dtos.CourseRequest;
import com.weolbu.LMS.dtos.CourseResponse;
import com.weolbu.LMS.entities.Course;
import com.weolbu.LMS.exceptions.DataNotFoundException;
import com.weolbu.LMS.repositories.CourseRepository;
import com.weolbu.LMS.repositories.RegistrationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("강의 생성 테스트")
    void createCourseTest() {
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName("Test Course");
        courseRequest.setMaxEnrollment(100L);
        courseRequest.setPrice(1000L);

        courseService.create(courseRequest);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("강의 목록 조회 pageable 테스트")
    void getListTest1() {
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "CREATED_DATE_TIME");
        Page<CourseResponse> coursePage = new PageImpl<>(List.of(CourseResponse.builder()
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
                .build()),pageable,2);

        when(courseRepository.findAllBy(pageable)).thenReturn(coursePage);

        Page<CourseResponse> list = courseService.getList(pageable);

        verify(courseRepository, times(1)).findAllBy(pageable);
        assertThat(list.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("강의 등록 테스트")
    void enrollTest() {
        String email = "test@example.com";
        List<Long> courseIdList = List.of(1L, 2L);

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(Course.builder().build()));

        courseService.enroll(email, courseIdList);

        verify(registrationRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("강의 등록 시 존재하지 않는 강의 조회 시 예외 발생 테스트")
    void getCoursesNotFoundTest() {
        String email = "notfound@example.com";
        List<Long> courseIdList = List.of(1L, 2L);

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            courseService.enroll(email, courseIdList);
        });
    }
}