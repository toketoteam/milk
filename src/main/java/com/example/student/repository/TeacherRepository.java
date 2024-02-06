package com.example.student.repository;

import com.example.student.entity.DirectorEntity;
import com.example.student.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {
    // 행의 수(카디널리티) 구함
    // : 해당 유치원을 선택한 학부모/선생님의 status 가 0인 행의 count 를 출력
    long countBySchoolNameAndStatusLike(String schoolName, int Status);


    List<TeacherEntity> findBySchoolNameAndStatusLike(String schoolName, int status);


    //String findClassNameByLoginId(String loginId);

    TeacherEntity findByLoginId(String loginId);

    List<TeacherEntity> findSchoolNameAndClassNameByLoginId(String loginId);

    Optional<TeacherEntity> findSchoolNameByLoginId(String loginId);
    Optional<TeacherEntity> findClassNameByLoginId(String loginId);

}
