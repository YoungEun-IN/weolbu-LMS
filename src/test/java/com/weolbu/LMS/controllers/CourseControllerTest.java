package com.weolbu.LMS.controllers;

import com.weolbu.LMS.dtos.CourseResponse;
import com.weolbu.LMS.services.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    @DisplayName("LECTURER 강의 생성 성공 테스트")
    @WithMockUser(roles = "LECTURER")
    void testCreateCourse1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "name",
                                    "maxEnrollment": 10,
                                    "price": "1000610"
                                }""")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("강의가 생성되었습니다."));
    }

    @Test
    @DisplayName("STUDENT 강의 생성 실패 테스트")
    @WithMockUser(roles = "STUDENT")
    void testCreateCourse2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "name",
                                    "maxEnrollment": 10,
                                    "price": "1000610"
                                }""")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    @WithMockUser(roles = {"LECTURER", "STUDENT"})
    @DisplayName("강의 목록 조회 테스트")
    void testGetCourseList() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CourseResponse> coursePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        Mockito.when(courseService.getList(any(Pageable.class))).thenReturn(coursePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("수강 신청 테스트")
    @WithMockUser(roles = {"LECTURER", "STUDENT"})
    void testEnrollCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/courses/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2, 3]")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}