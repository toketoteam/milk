package com.example.student.controller;

import com.example.student.dto.ParentDTO;
import com.example.student.dto.TeacherDTO;
import com.example.student.service.MemberService;
import com.example.student.service.ParentService;
import com.example.student.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    private final MemberService memberService;

    @PostMapping("/teacher/save")
    public String studentSave(@RequestParam Map<String, String> params,
                              @ModelAttribute TeacherDTO teacherDTO, HttpSession session){
        // db에 값저장
        System.out.println("teacherDTO="+teacherDTO);
        teacherService.save(teacherDTO);

        // 로그인한 사용자의 loginId 값 가져오기
        String loginId = (String) session.getAttribute("loginId");

        //parent테이블에서 role값 가져오기
        String role = teacherDTO.getRole();

        // user 테이블에 role 값 저장 (현재의 role정보를 loginId에 해당하는 걸 찾아서 role을 update해줌)
        memberService.updateMemberRole(loginId, role);

        //
        return "teacherMain";
    }

}
