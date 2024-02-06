package com.example.student.service;

import com.example.student.dto.AttendanceDTO;
import com.example.student.entity.AttendanceEntity;
import com.example.student.entity.ParentEntity;
import com.example.student.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    public void save(AttendanceDTO attendanceDTO) {
        AttendanceEntity attendanceEntity = AttendanceEntity.toAttendanceEntity(attendanceDTO);
        attendanceRepository.save(attendanceEntity);
    }

    public void updateStatus(long id, int status) {
        Optional<AttendanceEntity> optionalAttendance = attendanceRepository.findByParentId(id);
        optionalAttendance.ifPresent(attendance->{
            attendance.setStatus(status);
            attendanceRepository.save(attendance);
        });
    }


    // 나중에 attendanceStatus 도 수정하기
    // 해당 id의 해당 날짜를 가진 행의 inTime값을 수정
    public void updateInTime(long id,LocalDate date,LocalTime inTime) {
        // 해당 id를 가지며 혀재날짜인 경우
        Optional<AttendanceEntity> optionalAttendance = attendanceRepository.findByParentIdAndDate(id,date);
        optionalAttendance.ifPresent(attendance->{
            attendance.setInTime(inTime);
            attendanceRepository.save(attendance);
        });
    }

    public List<AttendanceEntity> findBySchoolNameAndClassNameAndStatusLike(String schoolName, String className,int status) {
        return attendanceRepository.findBySchoolNameAndClassNameAndStatusLike(schoolName,className,status);
    }


    public void updateDate(Long id, LocalDate currentDate) {
        AttendanceEntity attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
        attendance.setDate(currentDate);
        attendanceRepository.save(attendance);
    }



    public Optional<AttendanceEntity> findInTimeByParentIdAndDateLike(Long parentId, LocalDate date) {
        return attendanceRepository.findInTimeByParentIdAndDateLike(parentId,date);
    }


    public List<AttendanceEntity> findBySchoolNameAndClassNameAndStatusLikeAndDateLike(String schoolName, String className, int status, LocalDate date) {
        return attendanceRepository.findBySchoolNameAndClassNameAndStatusLikeAndDateLike(schoolName, className, status, date);
    }

    public Optional<AttendanceEntity> findInTimeById(Long id) {
        return attendanceRepository.findInTimeById(id);
    }

    public void updateOutTime(Long id, LocalTime outTime, String attendanceStatus,LocalDate date) {
        AttendanceEntity attendanceTime = (AttendanceEntity) attendanceRepository.findByParentIdAndDate(id,date)
                .orElseThrow(() -> new RuntimeException("id에 맞는 attendance 찾지 못했습니다: " + id));
        attendanceTime.setOutTime(outTime);
        attendanceTime.setAttendanceStatus(attendanceStatus);
        attendanceRepository.save(attendanceTime);
    }


    public void updateAttendanceStatus(Long id,String attendanceStatus,LocalDate date) {
        AttendanceEntity attendanceTime = attendanceRepository.findByParentIdAndDate(id,date)
                .orElseThrow(() -> new RuntimeException("id에 맞는 attendance 찾지 못했습니다: " + id));
        attendanceTime.setInTime(null);
        attendanceTime.setOutTime(null);
        attendanceTime.setAttendanceStatus(attendanceStatus);
        attendanceRepository.save(attendanceTime);
    }



//    public int countAttendanceByDateAndSchoolNameAndClassNameWithInTime(LocalDate date, String schoolName, String className) {
//        return attendanceRepository.countAttendanceByDateAndSchoolNameAndClassNameWithInTime(date,schoolName,className);
//    }

    public int countByDateAndSchoolNameAndClassNameAndInTimeIsNotNullAndStatusLike(LocalDate date, String schoolName, String className, int status) {
        return attendanceRepository.countByDateAndSchoolNameAndClassNameAndInTimeIsNotNullAndStatusLike(date,schoolName,className,status);
    }

    public int countByDateAndSchoolNameAndClassNameAndOutTimeIsNotNullAndStatusLike(LocalDate date, String schoolName, String className, int status) {
        return attendanceRepository.countByDateAndSchoolNameAndClassNameAndOutTimeIsNotNullAndStatusLike(date,schoolName,className,status);
    }

    public int countByDateAndSchoolNameAndClassNameAndAttendanceStatusLikeAndStatusLike(LocalDate date, String schoolName, String className, String attendanceStatus, int status) {
        return attendanceRepository.countByDateAndSchoolNameAndClassNameAndAttendanceStatusLikeAndStatusLike(date,schoolName,className,attendanceStatus,status);
    }

    public List<AttendanceEntity> findByParentIdAndDateLike(Long id, LocalDate date) {
        return attendanceRepository.findByParentIdAndDateLike(id,date);
    }

    public List<AttendanceEntity> findBySchoolNameAndStatusLikeAndDateLike(String schoolName, int status, LocalDate date) {
        return attendanceRepository.findBySchoolNameAndStatusLikeAndDateLike(schoolName,status,date);
    }

    public int countByDateAndSchoolNameAndInTimeIsNotNullAndStatusLike(LocalDate date, String schoolName,int status) {
        return attendanceRepository.countByDateAndSchoolNameAndInTimeIsNotNullAndStatusLike(date,schoolName,status);
    }

    public int countByDateAndSchoolNameAndOutTimeIsNotNullAndStatusLike(LocalDate date, String schoolName,int status) {
        return attendanceRepository.countByDateAndSchoolNameAndOutTimeIsNotNullAndStatusLike(date,schoolName,status);
    }

    public int countByDateAndSchoolNameAndAttendanceStatusLikeAndStatusLike(LocalDate date, String schoolName, String attendanceStatus,int status) {
        return attendanceRepository.countByDateAndSchoolNameAndAttendanceStatusLikeAndStatusLike(date,schoolName,attendanceStatus, status);
    }


    public int countBySchoolNameAndClassName(String schoolName, String className) {
        return attendanceRepository.countBySchoolNameAndClassName(schoolName,className);
    }
}
