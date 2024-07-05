package com.weolbu.LMS.repositories;

import com.weolbu.LMS.entities.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<Member> findOneWithRolesByEmail(String email);
}
