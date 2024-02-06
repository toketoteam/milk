package com.example.student.repository;

import com.example.student.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByLoginId(String LoginId);
    Optional<MemberEntity> findByRole(String Role);

}
