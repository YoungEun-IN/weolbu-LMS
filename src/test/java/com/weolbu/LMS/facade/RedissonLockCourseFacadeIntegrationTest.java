package com.weolbu.LMS.facade;

import com.weolbu.LMS.repositories.CourseRepository;
import com.weolbu.LMS.repositories.RegistrationRepository;
import com.weolbu.LMS.services.CourseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.weolbu.LMS.fixtures.CourseFixture.createCourse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedissonLockCourseFacadeIntegrationTest {

    @Autowired
    private RedissonLockCourseFacade redissonLockCourseFacade;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @BeforeEach
    public void insert() {
        courseRepository.saveAndFlush(createCourse());
    }

    @AfterEach
    public void delete() {
        registrationRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 10개 등록 요청 시 5개만 등록되는지 테스트")
    public void lockTest() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int userId = i;
            executorService.submit(() -> {
                try {
                    redissonLockCourseFacade.enroll(userId + "@gmail.com", List.of(1L));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(5, registrationRepository.countByCourseId(1L));
    }
}