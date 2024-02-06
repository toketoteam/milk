package com.example.student.entity;

import com.example.student.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="user")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String role;

    @Column(unique = true)
    private String loginId;

    @Column
    private String loginPassword;
    @Column
    private String userName;
    @Column
    private String userGender; //성별
    @Column
    private String userBirth; //생년월일
    @Column(name="userEmail")
    private String userEmail;
    @Column(name="userPhoneNumber")
    private String userPhoneNumber;

//    @Column
//    private String kindergarten; //유치원 이름
//    @Column
//    private String kindergartenArea; //주소 ;시도
//
//    @Column
//    private String kindergartenLocal;//주소:시도
//    @Column
//    private String areaDetail;//상세주소

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setLoginId(memberDTO.getLoginId());
        memberEntity.setRole(memberDTO.getRole());
        memberEntity.setLoginPassword(memberDTO.getLoginPassword());
        memberEntity.setUserName(memberDTO.getUserName());
        memberEntity.setUserBirth(memberDTO.getUserBirth());
        memberEntity.setUserGender(memberDTO.getUserGender());
        //성별 / 생년월일
        memberEntity.setUserEmail(memberDTO.getUserEmail());
        memberEntity.setUserPhoneNumber(memberDTO.getUserPhoneNumber());
//        //유치원 이름
//        memberEntity.setKindergarten(memberDTO.getKindergarten());
//        //유치원 주소
//        memberEntity.setKindergartenArea(memberDTO.getKindergartenArea());
//        memberEntity.setKindergartenLocal(memberDTO.getKindergartenLocal());
//        memberEntity.setAreaDetail(memberDTO.getAreaDetail());
        return memberEntity;
    }
}
