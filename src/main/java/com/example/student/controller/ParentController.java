package com.example.student.controller;

import com.example.student.dto.ParentDTO;
import com.example.student.entity.ParentEntity;
import com.example.student.service.MemberService;
import com.example.student.service.ParentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;
    private final MemberService memberService;

    @PostMapping("/student/save")
    public String studentSave(@RequestParam Map<String, String> params,
                              @ModelAttribute ParentDTO parentDTO, HttpSession session) {
        // db에 값저장
        System.out.println("parentDTO=" + parentDTO);
        parentService.save(parentDTO);

        // 로그인한 사용자의 loginId 값 가져오기
        String loginId = (String) session.getAttribute("loginId");

        //parent테이블에서 role값 가져오기
        String role = parentDTO.getRole();

        // user 테이블에 role 값 저장 (현재의 role정보를 loginId에 해당하는 걸 찾아서 role을 update해줌)
        memberService.updateMemberRole(loginId, role);

        //
        return "parentMain";
    }



}
