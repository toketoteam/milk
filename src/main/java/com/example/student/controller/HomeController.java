package com.example.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    // 로그아웃 => index 페이지로 이동
    @GetMapping("/")
    public String index(){
        return "index";
    }

    // 이전버튼 => 원장님 메인페이지로 이동
    @GetMapping("/main/back")
    public String mainBack(){
        return "main";
    }

    // 이전버튼 => 선생님 메인페이지로 이동
    @GetMapping("/teacher/main/back")
    public String teacherMainBack(){
        return "teacherMain";
    }

}
