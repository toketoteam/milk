package com.example.student.dto;

import com.example.student.entity.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
@Builder
public class MemberDTO {

    private long id;

    private String role;

    @NotBlank(message ="아이디를 입력해주세요.")
    @Pattern(regexp = "[a-zA-Z0-9]{2,9}",
            message = "아이디는 영문, 숫자만 가능하며 2 ~ 10자리까지 가능합니다.")
    private String loginId;

    @NotBlank(message ="비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}",
            message = "비밀번호는 영문과 숫자 조합으로 8 ~ 16자리까지 가능합니다.")
    private String loginPassword;
    @NotBlank(message ="이름을 입력해주세요.")
    private String userName;

    private String userGender; //성별

    private String userBirth;//생년월일

    @NotBlank(message ="이메일을 입력해주세요.")
    private String userEmail;

    @NotBlank(message ="전화번호를 입력해주세요.")
    private String userPhoneNumber;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setLoginId(memberEntity.getLoginId());
        memberDTO.setLoginPassword(memberEntity.getLoginPassword());
        memberDTO.setRole(memberEntity.getRole());
        memberDTO.setUserName(memberEntity.getUserName());
        memberDTO.setUserGender(memberEntity.getUserGender());
        memberDTO.setUserBirth(memberEntity.getUserBirth());
        memberDTO.setUserEmail(memberEntity.getUserEmail());
        memberDTO.setUserPhoneNumber(memberEntity.getUserPhoneNumber());
        return memberDTO;
    }

//    private String kindergarten; //유치원명
//
//    private String kindergartenArea; //시도
//
//    private String kindergartenLocal; //시도
//
//    private String areaDetail; // 상세주소

}
