package com.weolbu.LMS.repositories;

import com.weolbu.LMS.dtos.CourseResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseRepositoryCustom {
    List<CourseResponse> findAllBy(Pageable pageable);
}
