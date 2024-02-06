package com.example.student.repository;

import com.example.student.entity.AttendanceEntity;
import com.example.student.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity,Long>{

    List<AttendanceEntity> findBySchoolNameAndClassNameAndStatusLike(String schoolName, String className,int Status);

    Optional<AttendanceEntity> findByParentId(long id);

    Optional<AttendanceEntity> findInTimeByParentIdAndDateLike(Long parentId, LocalDate date);

    Optional<AttendanceEntity> findByParentIdAndDate(long id, LocalDate date);

    List<AttendanceEntity> findAllByParentId(Long parentId);

    List<AttendanceEntity> findBySchoolNameAndClassNameAndStatusLikeAndDateLike(String schoolName, String className, int status, LocalDate date);

    Optional<AttendanceEntity> findInTimeById(Long id);

    Optional<Object> findByIdAndDateLike(Long id, LocalDate date);

    int countByDateAndSchoolNameAndClassNameAndInTimeIsNotNullAndStatusLike(LocalDate date, String schoolName, String className,int status);

    int countByDateAndSchoolNameAndClassNameAndOutTimeIsNotNullAndStatusLike(LocalDate date, String schoolName, String className,int status);

    int countByDateAndSchoolNameAndClassNameAndAttendanceStatusLikeAndStatusLike(LocalDate date, String schoolName, String className, String attendanceStatus,int status);

    List<AttendanceEntity> findByParentIdAndDateLike(Long id, LocalDate date);

    List<AttendanceEntity> findBySchoolNameAndStatusLikeAndDateLike(String schoolName, int status, LocalDate date);

    int countByDateAndSchoolNameAndInTimeIsNotNullAndStatusLike(LocalDate date, String schoolName,int status);

    int countByDateAndSchoolNameAndOutTimeIsNotNullAndStatusLike(LocalDate date, String schoolName, int status);

    int countByDateAndSchoolNameAndAttendanceStatusLikeAndStatusLike(LocalDate date, String schoolName, String attendanceStatus, int status);

    int countBySchoolNameAndClassName(String schoolName, String className);
}
