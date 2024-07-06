package com.weolbu.LMS.facade;

import com.weolbu.LMS.entities.Course;
import com.weolbu.LMS.entities.Registration;
import com.weolbu.LMS.repositories.CourseRepository;
import com.weolbu.LMS.repositories.RegistrationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedissonLockStockFacadeTest {

    @Autowired
    private RedissonLockCourseFacade redissonLockCourseFacade;

    @MockBean
    private RedissonClient redissonClient;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @BeforeEach
    public void insert() {
        Course course = Course.builder()
                .id(1L)
                .price(10000L)
                .name("강의1")
                .maxEnrollment(100L)
                .build();
        courseRepository.saveAndFlush(course);

        Registration registration = Registration.builder()
                .email("test@gmail.com")
                .course(course)
                .build();
        registrationRepository.saveAndFlush(registration);
    }

    @AfterEach
    public void delete() {
        registrationRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 1000개 등록 요청 테스트")
    public void lockTest() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockCourseFacade.enroll("test@gmail.com", List.of(1L));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Registration registration = registrationRepository.findById(1L).orElseThrow();

        assertEquals(100, registration.getCourse().getMaxEnrollment());
    }
}