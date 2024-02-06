package com.example.student.repository;

import com.example.student.entity.AttendanceEntity;
import com.example.student.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ParentRepository extends JpaRepository<ParentEntity, Long> {
    // 행의 수(카디널리티) 구함
    // : 해당 유치원을 선택한 status 가 1인 행의 count 를 출력
    long countBySchoolNameAndStatusLike(String schoolName, int Status);


    List<ParentEntity> findBySchoolNameAndStatusLike(String schoolName, int status);

    List<ParentEntity> findByIdAndStatusLike(long id, int status);


    List<ParentEntity> findBySchoolNameAndClassNameAndStatusLike(String schoolName, String className, int status);


    Optional<ParentEntity> findSchoolNameByLoginId(String loginId);

    Optional<ParentEntity> findClassNameByLoginId(String loginId);

    Optional<ParentEntity> findIdByLoginId(String loginId);


   // Optional<ParentEntity> findIdBySchoolNameAndClassName(String schoolName, String className);

    List<ParentEntity> findBySchoolNameAndClassName(String schoolName, String deleteClass);

    Optional<ParentEntity> findIdBySchoolNameAndClassName(String schoolName, String className);

    int countBySchoolNameAndClassName(String schoolName, String className);
}
