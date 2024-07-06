package com.weolbu.LMS.dtos;

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
}
