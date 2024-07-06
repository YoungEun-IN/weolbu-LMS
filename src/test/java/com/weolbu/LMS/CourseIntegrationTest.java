package com.weolbu.LMS;

import com.weolbu.LMS.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseIntegrationTest {
    @Autowired
    MemberRepository memberRepository;

}
