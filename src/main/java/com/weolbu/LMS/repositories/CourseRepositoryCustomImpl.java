package com.weolbu.LMS.repositories;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weolbu.LMS.dtos.CourseResponse;
import com.weolbu.LMS.enums.CourseSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.weolbu.LMS.entities.QCourse.course;
import static com.weolbu.LMS.entities.QRegistration.registration;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CourseRepositoryCustomImpl implements CourseRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<CourseResponse> findAllBy(Pageable pageable) {
        return jpaQueryFactory
                .select(Projections.constructor(CourseResponse.class,
                        course.id,
                        course.name,
                        course.maxEnrollment,
                        course.price,
                        course.createdDateTime,
                        registration.count().as("registrationCount")),
                .from(course)
                .leftJoin(registration).on(course.id.eq(registration.course.id))
                .groupBy(course.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifier(pageable.getSort()))
                .fetch();
    }

    private OrderSpecifier<?> createOrderSpecifier(Sort sort) {
        Sort.Order order = sort.stream().findFirst().orElse(Sort.Order.desc("id"));

        return switch (order.getProperty()) {
            case "CREATED_DATE_TIME" -> new OrderSpecifier<>(Order.DESC, course.createdDateTime);
            case "REGISTRATION_COUNT" -> new OrderSpecifier<>(Order.DESC, registration.count());
            //case "REGISTRATION_RATE" -> new OrderSpecifier<>(Order.DESC, item.price);
//            case DISCOUNT -> new OrderSpecifier<>(Order.DESC, item.discount);
            default -> new OrderSpecifier<>(Order.DESC, course.id);
        };
    }
}
