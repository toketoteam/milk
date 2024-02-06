package com.example.student.dto;

import com.example.student.entity.ParentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
@Builder
public class ParentDTO {

    private long id;

    private String loginId;

    private String role;

    private String parentName;

    private String schoolName;

    private String studentName;

    private String studentGender;

    private int studentBirth;

    private String className;

    private int status; //유치원 승인=1 or 거부=2 or 대기=null
    public static ParentDTO toParentDTO(ParentEntity parentEntity){
        ParentDTO parentDTO = new ParentDTO();

        parentDTO.setLoginId(parentEntity.getLoginId());
        parentDTO.setRole(parentEntity.getRole());
        parentDTO.setParentName(parentEntity.getParentName());
        parentDTO.setSchoolName(parentEntity.getSchoolName());
        parentDTO.setStudentName(parentEntity.getStudentName());
        parentDTO.setStudentGender(parentEntity.getStudentGender());
        parentDTO.setStudentBirth(parentEntity.getStudentBirth());
        parentDTO.setClassName(parentEntity.getClassName());
        parentDTO.setStatus(parentEntity.getStatus());

        return parentDTO;
    }
}
