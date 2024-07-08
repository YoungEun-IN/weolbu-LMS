package com.weolbu.LMS.dtos;

import com.weolbu.LMS.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    @NotNull
    private String name;

    @NotNull
    private Long maxEnrollment;

    @NotNull
    private Long price;

    public static Course buildCourse(CourseRequest courseRequest) {
        return Course.builder()
                .name(courseRequest.getName())
                .maxEnrollment(courseRequest.getMaxEnrollment())
                .price(courseRequest.getPrice())
                .build();
    }
}
