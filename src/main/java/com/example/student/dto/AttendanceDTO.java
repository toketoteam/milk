package com.example.student.dto;

import com.example.student.entity.AttendanceEntity;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
@Builder
public class AttendanceDTO {

    private long id;

    private long parentId;
    private String schoolName;
    private String studentName;

    private String className;


    private LocalDate date;


    private LocalTime inTime;


    private LocalTime outTime;


    private String attendanceStatus;
    private int status; //유치원 승인=1 or 거부=2 or 대기=null

    public static AttendanceDTO toAttendanceDTO(AttendanceEntity attendanceEntity){
        AttendanceDTO attendanceDTO = new AttendanceDTO();

        attendanceDTO.setId(attendanceEntity.getId());
        attendanceDTO.setParentId(attendanceEntity.getParentId());
        attendanceDTO.setSchoolName(attendanceEntity.getSchoolName());
        attendanceDTO.setStudentName(attendanceEntity.getStudentName());
        attendanceDTO.setClassName(attendanceEntity.getClassName());
        attendanceDTO.setDate(attendanceEntity.getDate());
        attendanceDTO.setInTime(attendanceEntity.getInTime());
        attendanceDTO.setOutTime(attendanceEntity.getOutTime());
        attendanceDTO.setStatus(attendanceEntity.getStatus());
        attendanceDTO.setAttendanceStatus(attendanceEntity.getAttendanceStatus());

        return attendanceDTO;
    }


}
