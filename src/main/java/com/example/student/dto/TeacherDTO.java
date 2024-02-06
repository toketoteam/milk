package com.example.student.dto;

import com.example.student.entity.TeacherEntity;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
@Builder
public class TeacherDTO {
    private long id;

    private String loginId;

    private String role;

    private String schoolName;

    private String className;

    private String teacherName;

    private int status; //유치원 승인=1 or 거부=2 or 대기=null

    public static TeacherDTO toTeacherDTO(TeacherEntity teacherEntity){
        TeacherDTO teacherDTO = new TeacherDTO();

        teacherDTO.setLoginId(teacherEntity.getLoginId());
        teacherDTO.setRole(teacherEntity.getRole());
        teacherDTO.setSchoolName(teacherEntity.getSchoolName());
        teacherDTO.setClassName(teacherEntity.getClassName());
        teacherDTO.setTeacherName(teacherEntity.getTeacherName());
        teacherDTO.setStatus(teacherEntity.getStatus());

        return  teacherDTO;
    }
}
