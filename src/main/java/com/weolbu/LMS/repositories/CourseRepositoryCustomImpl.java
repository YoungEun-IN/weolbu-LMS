package com.weolbu.LMS.repositories;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weolbu.LMS.dtos.CourseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static com.weolbu.LMS.entities.QCourse.course;
import static com.weolbu.LMS.entities.QRegistration.registration;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CourseRepositoryCustomImpl implements CourseRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<CourseResponse> findAllBy(Pageable pageable) {
        List<CourseResponse> courseResponseList = jpaQueryFactory
                .select(Projections.constructor(CourseResponse.class,
                        course.id,
                        course.name,
                        course.maxEnrollment,
                        course.price,
                        course.createdDateTime,
                        registration.count().as("registrationCount"),
                        numberTemplate(Double.class, "({0} * 1.0) / {1}", registration.count(), course.maxEnrollment).as("registrationRate")))
                .from(course)
                .leftJoin(registration).on(course.id.eq(registration.course.id))
                .groupBy(course.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifier(pageable.getSort()))
                .fetch();

        long total = jpaQueryFactory
                .select(course.id)
                .from(course)
                .fetchCount();

        return new PageImpl<>(courseResponseList, pageable, total);
    }

    private OrderSpecifier<?> createOrderSpecifier(Sort sort) {
        Sort.Order order = sort.stream().findFirst().orElse(Sort.Order.desc("id"));

        return switch (order.getProperty()) {
            case "CREATED_DATE_TIME" -> new OrderSpecifier<>(Order.DESC, course.createdDateTime);
            case "REGISTRATION_COUNT" -> new OrderSpecifier<>(Order.DESC, registration.count());
            case "REGISTRATION_RATE" -> new OrderSpecifier<>(Order.DESC, registration.count().divide(course.maxEnrollment));
            default -> new OrderSpecifier<>(Order.DESC, course.id);
        };
    }
}
