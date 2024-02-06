package com.example.student.entity;

import com.example.student.dto.AttendanceDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name="attendance") //출석테이블
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="parentId")
    private long parentId;

    @Column(name="studentName")
    private String studentName;
    @Column(name="schoolName")
    private String schoolName;
    @Column(name="className")
    private String className;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "in_time")
    private LocalTime inTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime outTime;

    @Column
    private String attendanceStatus;

    @Column
    private int status; //유치원 승인=1 or 거부=2 or 대기=0

    public static AttendanceEntity toAttendanceEntity(AttendanceDTO attendanceDTO){
        AttendanceEntity attendanceEntity = new AttendanceEntity();

        attendanceEntity.setId(attendanceDTO.getId());
        attendanceEntity.setParentId(attendanceDTO.getParentId());
        attendanceEntity.setStudentName(attendanceDTO.getStudentName());
        attendanceEntity.setClassName(attendanceDTO.getClassName());
        attendanceEntity.setSchoolName(attendanceDTO.getSchoolName());
        attendanceEntity.setDate(attendanceDTO.getDate());
        attendanceEntity.setInTime(attendanceDTO.getInTime());
        attendanceEntity.setOutTime(attendanceDTO.getOutTime());
        attendanceEntity.setStatus(attendanceDTO.getStatus());
        attendanceEntity.setAttendanceStatus(attendanceDTO.getAttendanceStatus());

        return  attendanceEntity;
    }
}
