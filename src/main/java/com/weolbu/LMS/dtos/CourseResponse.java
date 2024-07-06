package com.weolbu.LMS.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String name;
    private Long maxEnrollment;
    private Long price;
    private LocalDateTime createdDateTime;
    private Long registrationCount;
    private Double registrationRate;
}
