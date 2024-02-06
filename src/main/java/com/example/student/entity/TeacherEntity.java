package com.example.student.entity;

import com.example.student.dto.ParentDTO;
import com.example.student.dto.TeacherDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="teacher")
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="loginId")
    private String loginId;

    @Column(name="role")
    private String role;

    @Column
    private String schoolName;

    @Column
    private String className;

    @Column
    private String teacherName;

    @Column
    private int status; //유치원 승인=1 or 거부=2 or 대기=null

    public static TeacherEntity toTeacherEntity(TeacherDTO teacherDTO){
        TeacherEntity teacherEntity = new TeacherEntity();

        teacherEntity.setLoginId(teacherDTO.getLoginId());
        teacherEntity.setRole(teacherDTO.getRole());
        teacherEntity.setSchoolName(teacherDTO.getSchoolName());
        teacherEntity.setClassName(teacherDTO.getClassName());
        teacherEntity.setTeacherName(teacherDTO.getTeacherName());
        teacherEntity.setStatus(teacherDTO.getStatus());

        return  teacherEntity;
    }
}
