package com.weolbu.LMS.repositories;

import com.weolbu.LMS.entities.Role;
import com.weolbu.LMS.enums.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByMemberType(MemberType memberType);
}
