package com.weolbu.LMS.facade;

import com.weolbu.LMS.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockCourseFacade {

    private final RedissonClient redissonClient;
    private final CourseService courseService;

    public void enroll(String email, List<Long> courseIdList) {
        RLock lock = redissonClient.getLock("key");

        try {
            boolean available = lock.tryLock(2, 1, TimeUnit.SECONDS);

            if (!available) {
                log.error("lock 획득 실패");
                return;
            }

            courseService.enroll(email, courseIdList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
