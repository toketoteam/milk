package com.example.student.dto;

import com.example.student.entity.DirectorEntity;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
@Builder
public class DirectorDTO {
    private long id;

    private String role;

    private String loginId;

    private String directorName;

    private String schoolName;

    private String SchoolNumber;

    private String postcode;

    private String address;

    private String detailAddress;

    private String extraAddress;

    private String className;

    private LocalTime studentInTime; // 원생 등원시간

    private LocalTime studentOutTime; //원생 하원시간

    private LocalTime teacherInTime; // 원생 등원시간

    private LocalTime teacherOutTime; //원생 하원시간

    public static DirectorDTO toDirectorDTO(DirectorEntity directorEntity){
        DirectorDTO directorDTO = new DirectorDTO();

        directorDTO.setId(directorEntity.getId());
        directorDTO.setRole(directorEntity.getRole());
        directorDTO.setLoginId(directorEntity.getLoginId());
        directorDTO.setDirectorName(directorEntity.getDirectorName());
        directorDTO.setSchoolName(directorEntity.getSchoolName());
        directorDTO.setSchoolNumber(directorEntity.getSchoolNumber());
        directorDTO.setClassName(directorEntity.getClassName());
        directorDTO.setAddress(directorEntity.getAddress());
        directorDTO.setPostcode(directorEntity.getPostcode());
        directorDTO.setDetailAddress(directorEntity.getDetailAddress());
        directorDTO.setExtraAddress(directorEntity.getExtraAddress());

        directorDTO.setStudentInTime(directorEntity.getStudentInTime());
        directorDTO.setStudentOutTime(directorEntity.getStudentOutTime());

        directorDTO.setTeacherInTime(directorEntity.getTeacherInTime());
        directorDTO.setTeacherOutTime(directorEntity.getTeacherOutTime());

        return directorDTO;
    }

}
