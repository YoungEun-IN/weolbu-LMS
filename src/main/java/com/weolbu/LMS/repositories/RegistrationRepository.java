package com.weolbu.LMS.repositories;

import com.weolbu.LMS.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Long countByCourseId(Long courseId);
}
