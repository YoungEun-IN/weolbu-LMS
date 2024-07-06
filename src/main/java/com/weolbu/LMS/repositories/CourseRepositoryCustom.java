package com.weolbu.LMS.repositories;

import com.weolbu.LMS.dtos.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {
    Page<CourseResponse> findAllBy(Pageable pageable);
}
