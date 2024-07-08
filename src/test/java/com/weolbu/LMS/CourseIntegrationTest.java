package com.weolbu.LMS;

import com.weolbu.LMS.entities.Course;
import com.weolbu.LMS.repositories.CourseRepository;
import com.weolbu.LMS.repositories.RegistrationRepository;
import com.weolbu.LMS.services.CourseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.weolbu.LMS.fixtures.CourseFixture.createCourseList;
import static com.weolbu.LMS.fixtures.CourseFixture.createRegistrationList;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @BeforeEach
    public void insert() {
        List<Course> courseList = createCourseList(3);
        courseRepository.saveAll(courseList);
        registrationRepository.saveAll(createRegistrationList(courseList));
    }

    @AfterEach
    public void delete() {
        registrationRepository.deleteAll();
        registrationRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"LECTURER", "STUDENT"})
    @DisplayName("강의 목록 조회 테스트")
    void getCourseListTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses")
                        .param("page","0")
                        .param("size","2")
                        .param("sort","CREATED_DATE_TIME,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$..content.length()").value(2))
                .andExpect(jsonPath("$..content[0].id").value(3))
                .andExpect(jsonPath("$..content[0].registrationRate").value(0.2))
                .andExpect(jsonPath("$..content[1].id").value(2))
                .andExpect(jsonPath("$..content[1].registrationRate").value(0.2))
                .andDo(print());
    }

}
