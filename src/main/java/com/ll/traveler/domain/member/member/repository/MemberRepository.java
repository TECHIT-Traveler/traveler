package com.ll.traveler.domain.member.member.repository;

import com.ll.traveler.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findById(Long id);

    Member findByUsernameAndEmail(String username, String email);
}