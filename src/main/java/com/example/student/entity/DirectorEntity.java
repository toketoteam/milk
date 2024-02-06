package com.example.student.entity;

import com.example.student.dto.DirectorDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name="director") //원장테이블
public class DirectorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="loginId")
    private String loginId;

    //directory의 role값을 member에 보내주어야함
    @Column(name="role")
    private String role;
    @Column
    private String schoolName;

    @Column
    private String directorName;

    //원 전화번호
    @Column
    private String SchoolNumber;

    //우편번호
    @Column
    private String postcode;

    //지번주소
    @Column
    private String address;

    //상세주소
    @Column
    private String detailAddress;

    //동
    @Column
    private String extraAddress;

    @Column
    private String className;

    @Override
    public String toString() {
        return className;
    }

    @JsonFormat(pattern = "HH:mm")
    private LocalTime studentInTime; // 원생 등원시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime studentOutTime; //원생 하원시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime teacherInTime; // 원생 등원시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime teacherOutTime; //원생 하원시간


    public static DirectorEntity toDirectorEntity(DirectorDTO directorDTO){
        DirectorEntity directorEntity = new DirectorEntity();

        directorEntity.setLoginId(directorDTO.getLoginId());
        directorEntity.setRole(directorDTO.getRole());
        directorEntity.setSchoolName(directorDTO.getSchoolName());
        directorEntity.setSchoolNumber(directorDTO.getSchoolNumber());
        directorEntity.setClassName(directorDTO.getClassName());
        directorEntity.setAddress(directorDTO.getAddress());
        directorEntity.setPostcode(directorDTO.getPostcode());
        directorEntity.setDirectorName(directorDTO.getDirectorName());
        directorEntity.setDetailAddress(directorDTO.getDetailAddress());
        directorEntity.setExtraAddress(directorDTO.getExtraAddress());

        directorEntity.setStudentInTime(directorDTO.getStudentInTime());
        directorEntity.setStudentOutTime(directorDTO.getStudentOutTime());

        directorEntity.setTeacherInTime(directorDTO.getTeacherInTime());
        directorEntity.setTeacherOutTime(directorDTO.getTeacherOutTime());
        return  directorEntity;
    }


}
