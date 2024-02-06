package com.example.student.entity;

import com.example.student.dto.DirectorDTO;
import com.example.student.dto.ParentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name="parent")
public class ParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="loginId")
    private String loginId;

    @Column(name="role")
    private String role;
    @Column
    private String parentName;
    @Column
    private String schoolName;
    @Column
    private String studentName;
    @Column
    private String studentGender;
    @Column
    private int studentBirth;
    @Column
    private String className;
    @Column
    private int status; //유치원 승인=1 or 거부=2 or 대기=0

    public static ParentEntity toParentEntity(ParentDTO parentDTO){
        ParentEntity parentEntity = new ParentEntity();

        parentEntity.setLoginId(parentDTO.getLoginId());
        parentEntity.setRole(parentDTO.getRole());
        parentEntity.setParentName(parentDTO.getParentName());
        parentEntity.setSchoolName(parentDTO.getSchoolName());
        parentEntity.setStudentName(parentDTO.getStudentName());
        parentEntity.setClassName(parentDTO.getClassName());
        parentEntity.setStudentGender(parentDTO.getStudentGender());
        parentEntity.setStudentBirth(parentDTO.getStudentBirth());
        parentEntity.setStatus(parentDTO.getStatus());

        return  parentEntity;
    }
}
