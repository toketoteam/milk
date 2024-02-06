package com.example.student.service;


import com.example.student.dto.MemberDTO;
import com.example.student.entity.MemberEntity;
import com.example.student.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.ClassUtils.isPresent;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO){
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }


    public String idCheck(String loginId) {
        Optional<MemberEntity> byLoginId = memberRepository.findByLoginId(loginId);
        if(byLoginId.isPresent()){
            //조회결과가 있으면 사용불가능
            return null;
        }else{
            //조회결과가 없으면 사용 가능
            return "ok";
        }
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> byloginId = memberRepository.findByLoginId(memberDTO.getLoginId());
        if(byloginId.isPresent()) {
            MemberEntity memberEntity = byloginId.get();
            if(memberEntity.getLoginPassword().equals(memberDTO.getLoginPassword())){
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;

            }else {
                return null;
            }
        }else{
            return null;
        }
    }

    public MemberDTO Role(String role){
        Optional<MemberEntity> byRole = memberRepository.findByRole(role);
        return null;
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList=memberRepository.findAll();
        //entity가 여러개 담긴 객체를 dto가 여러개담긴 것으로 저장
        List<MemberDTO> memberDTOList=new ArrayList<>();
        for(MemberEntity memberEntity: memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }



    //login에따른 role update
    public void updateMemberRole(String loginId, String role) {
        // 로그인 ID를 기반으로 member 엔티티를 찾아와서 role 값을 업데이트
        Optional<MemberEntity> optionalMember = memberRepository.findByLoginId(loginId);
        optionalMember.ifPresent(member -> {
            member.setRole(role);
            memberRepository.save(member);
        });
    }






}
