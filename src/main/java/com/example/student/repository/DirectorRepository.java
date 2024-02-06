package com.example.student.repository;

import com.example.student.entity.DirectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DirectorRepository extends JpaRepository<DirectorEntity,Long> {
   // Optional<MemberEntity> findByClassName(String className);

    // schoolName 에 해당하는 className 찾음
    List<DirectorEntity> findClassNamesBySchoolName(String schoolName);

    // 유치원 검색(parent / teacher 유치원선택에서 사용)
    // : schoolName,address, detailAddress 중에 검색어에 해당하는 값이 있는 것을 찾음
    List<DirectorEntity> findBySchoolNameContainingOrAddressContainingOrDetailAddressContainingIgnoreCase(String schoolName, String address, String detailAddress);


    List<DirectorEntity> findSchoolNamesByLoginId(String loginId);


    List<DirectorEntity> findBySchoolName(String schoolName);

    List<DirectorEntity> findByLoginId(String loginId);

    List<DirectorEntity> findBySchoolNameAndClassName(String schoolName, String className);

    List<DirectorEntity> findTeacherInTimeAndTeacherOutTimeBySchoolNameAndClassName(String schoolName, String className);

    List<DirectorEntity> findStudentInTimeAndStudentOutTimeBySchoolNameAndClassName(String schoolName, String className);

    DirectorEntity getTeacherByLoginId(String loginId);

    void deleteByLoginIdAndSchoolNameAndClassName(String loginId, String schoolName, String className);

    void deleteBySchoolNameAndClassName(String schoolName, String className);

    Optional<DirectorEntity> findIdBySchoolNameAndClassName(String schoolName, String className);
}
