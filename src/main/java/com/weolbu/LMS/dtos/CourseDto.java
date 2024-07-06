package com.weolbu.LMS.dtos;

import com.weolbu.LMS.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    @NotNull
    private String name;

    @NotNull
    private Long maxEnrollment;

    @NotNull
    private Long price;

    private LocalDateTime createdDateTime;

    public static CourseDto fromEntity(Course course) {
        return CourseDto.builder()
                .name(course.getName())
                .maxEnrollment(course.getMaxEnrollment())
                .price(course.getPrice())
                .createdDateTime(course.getCreatedDateTime())
                .build();
    }
}
