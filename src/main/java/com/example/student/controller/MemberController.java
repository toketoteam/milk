package com.example.student.controller;

import com.example.student.dto.DirectorDTO;
import com.example.student.dto.MemberDTO;
import com.example.student.dto.ParentDTO;
import com.example.student.entity.*;
import com.example.student.repository.AttendanceRepository;
import com.example.student.repository.DirectorRepository;
import com.example.student.repository.ParentRepository;
import com.example.student.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.swing.text.html.Option;
import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor

public class MemberController {

   private final MemberService memberService;
   private final DirectorService directorService;
   private final ParentService parentService;
   private final TeacherService teacherService;
   private final AttendanceService attendanceService;
   @GetMapping("/member/save")
    public String saveForm(){
       return "save";
   }

   //db값 저장 및 출력
   @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
       System.out.println("MemberController.save");
       System.out.println("memberDTO="+memberDTO);
       memberService.save(memberDTO);
       return "index";
   }

   @GetMapping("/member/role")
   public String saveRole(){
      return "role";
   }
   //회원정보 모두 가져오기
   @GetMapping("/member/")
   public String findAll(Model model){
      List<MemberDTO> memberDTOList=memberService.findAll();
      //어떠한 html로 가져갈 데이터가 있다면 model사용
      model.addAttribute("memberList",memberDTOList);
      return "list";
   }

   @GetMapping("/member/login")
   public String loginForm(){
      return "login";
   }

   //ajax를 사용하면 @RespomseBody 꼭 사용해줘야한다
   @PostMapping("/member/id-check")
   public @ResponseBody String idCheck(@RequestParam("loginId") String loginId){
      System.out.println("idCheck = "+loginId);
      String checkResult = memberService.idCheck(loginId);
      return checkResult;
   }

   @PostMapping("/loginId/save")
   public String setRole(Model model,@RequestParam String loginId, @RequestParam String role, HttpSession session) {
      // 세션에 데이터 저장
      session.setAttribute("loginId", loginId);
      session.setAttribute("role",role);

      if ("원장님".equals(role)) {
         System.out.println("loginId:"+loginId+" role: director");
         return "director";

      } else if ("학부모".equals(role) ||"선생님".equals(role) ) {
         System.out.println("loginId:" + loginId + " role"+role);
         List<DirectorDTO> directorDTOList = directorService.findSchool();

         // Java 스트림을 사용하여 schoolName을 기준으로 중복된 값 제거
         List<DirectorDTO> uniqueDirectorDTOList = directorDTOList.stream()
                 .collect(Collectors.toMap(DirectorDTO::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                 .values()
                 .stream()
                 .collect(Collectors.toList());

         // parent.html에 memberList값 전달
         model.addAttribute("memberList", uniqueDirectorDTOList);
         return "selectSchool";

      }
      return "index";
   }

   @PostMapping("/member/login")
   public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,Model model){
      //login메서드 호출
      MemberDTO loginResult = memberService.login(memberDTO);
      //parent의 status를 찾아오기
      //찾아온 status중 0인사용자의 count

      if (loginResult != null) {
         // login 성공
         // loginId 정보 세션에 저장
         session.setAttribute("loginId", loginResult.getLoginId());
         session.setAttribute("userName",loginResult.getUserName());
         System.out.println("loginID : " + loginResult.getLoginId()); //현재 로그인된 아이디 출력
         System.out.println("role : "+loginResult.getRole());

         //세션 유지시간 설정 60*30 => 1800초 ==30분
         session.setMaxInactiveInterval(60*30);
         System.out.println("login success");
         // role이 null이면 역할지정 페이지로 이동
         LocalDate currentDate = LocalDate.now(); // 현재날짜
         if (loginResult.getRole() == null ) {
            //redirectAttributes.addFlashAttribute("message", "Role is null. Redirecting to the main page.");
            return "role";
         }
         //role에 따라 이동하는 html 다르다.
         else{
            if("parent".equals(loginResult.getRole())){
               // 로긘된 사용자의 schoolName 이랑 className 출력
               Optional<ParentEntity> sName = parentService.findSchoolNameByLoginId(loginResult.getLoginId());
               String schoolName = sName.get().getSchoolName();
               Optional<ParentEntity> cName = parentService.findClassNameByLoginId(loginResult.getLoginId());
               String className = cName.get().getClassName();
               //현재 director 테이블에 정해진 studentInTime 과 studentTime 을 출력
               List<DirectorEntity> studentTime = directorService.findStudentInTimeAndStudentOutTimeBySchoolNameAndClassName(schoolName,className);
               model.addAttribute("studentTime",studentTime);

               // 현재로그인된 사용자의 id를 가져와서 parentId에  저장
               Optional<ParentEntity> id = parentService.findIdByLoginId(loginResult.getLoginId());
               Long parentId = id.get().getId();
               // parentId와 현재 날짜와 일치한는 행을( list ) attendance 테이블에서 찾아오기
               List<AttendanceEntity> studentAttendance = attendanceService.findByParentIdAndDateLike(parentId, currentDate);
               if (studentAttendance.isEmpty()) {
                  model.addAttribute("studentAttendance", Collections.emptyList());
               } else {
                  model.addAttribute("studentAttendance", studentAttendance);
               }


               return"parentMain";

            }
            // teacher 페이지로 이동
            else if("teacher".equals(loginResult.getRole())){
               //로그인 한 사용자의 schoolName 과 className 출력하기
               List<TeacherEntity> schoolAndClassName = teacherService.findSchoolNameAndClassNameByLoginId(loginResult.getLoginId());
               model.addAttribute("schoolAndClassName",schoolAndClassName);
               Optional<TeacherEntity> sName = teacherService.findSchoolNameByLoginId(loginResult.getLoginId());
               String schoolName = sName.get().getSchoolName();
               Optional<TeacherEntity> cName = teacherService.findClassNameByLoginId(loginResult.getLoginId());
               String className = cName.get().getClassName();
               //System.out.println("schoolNAME:"+schoolName+"className:"+className);

               // 등원한 학생의 수
               int okCount = attendanceService.countByDateAndSchoolNameAndClassNameAndInTimeIsNotNullAndStatusLike(currentDate,schoolName, className,1);
               // 하원한 학생의 수
               int outCount = attendanceService.countByDateAndSchoolNameAndClassNameAndOutTimeIsNotNullAndStatusLike(currentDate,schoolName, className,1);
               // 하원한 학생의 수
               int safeCount = attendanceService.countByDateAndSchoolNameAndClassNameAndAttendanceStatusLikeAndStatusLike(currentDate,schoolName, className,"출석",1);
               // 결석한 학생의 수
               int noCount = attendanceService.countByDateAndSchoolNameAndClassNameAndAttendanceStatusLikeAndStatusLike(currentDate,schoolName, className,"결석",1);
               // 조퇴한 학생의 수
               int earlyCount = attendanceService.countByDateAndSchoolNameAndClassNameAndAttendanceStatusLikeAndStatusLike(currentDate,schoolName, className,"조퇴",1);
               // 지각한 학생의 수
               int lateCount = attendanceService.countByDateAndSchoolNameAndClassNameAndAttendanceStatusLikeAndStatusLike(currentDate,schoolName, className,"지각",1);

               model.addAttribute("okCount",okCount);
               model.addAttribute("outCount",outCount);
               model.addAttribute("safeCount",safeCount);
               model.addAttribute("noCount",noCount);
               model.addAttribute("earlyCount",earlyCount);
               model.addAttribute("lateCount",lateCount);
               // 교직원의 출퇴근시간 보기
               // schoolName 과 className 일치하는 것의 리스트를 출력
               List<DirectorEntity> teacherTime = directorService.findTeacherInTimeAndTeacherOutTimeBySchoolNameAndClassName(schoolName,className);
               model.addAttribute("teacherTime",teacherTime);

               return"teacherMain";


            }
            else{ // 원장님
               // 원장님의 loginId를 기반으로 schoolName 가져오기 (중복제거해서)
               List<DirectorEntity> schoolName = directorService.findSchoolNameByLoginId(loginResult.getLoginId());

               // schoolName 을 기준으로 중복제거 -> schoolName 한개만 출력
               List<DirectorEntity> schoolNameList = schoolName.stream()
                       .collect(Collectors.toMap(DirectorEntity::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                       .values()
                       .stream()
                       .collect(Collectors.toList());
               model.addAttribute("schoolName", schoolNameList);

               // schoolName
               String result = schoolNameList.get(0).getSchoolName();


               // status가 0인(승인대기) parent의 행의 수
               long parentWaitCount = parentService.countBySchoolNameAndStatusLike(result, 0);
               model.addAttribute("parentWaitCount", parentWaitCount);

               // status가 0인(승인대기) parent의 행의 수
               long parentCount = parentService.countBySchoolNameAndStatusLike(result, 1);
               model.addAttribute("parentCount", parentCount);

               // status가 0인(승인대기) teacher의 행의 수
               long teacherWaitCount = teacherService.countBySchoolNameAndStatusLike(result, 0);
               model.addAttribute("teacherWaitCount", teacherWaitCount);

               // status가 0인(승인대기) parent의 행의 수
               long teacherCount = teacherService.countBySchoolNameAndStatusLike(result, 1);
               model.addAttribute("teacherCount", teacherCount);

               // 등원한 학생의 수
               int okCount = attendanceService.countByDateAndSchoolNameAndInTimeIsNotNullAndStatusLike(currentDate,result,1);
               // 하원한 학생의 수
               int outCount = attendanceService.countByDateAndSchoolNameAndOutTimeIsNotNullAndStatusLike(currentDate,result,1);
               // 하원한 학생의 수
               int safeCount = attendanceService.countByDateAndSchoolNameAndAttendanceStatusLikeAndStatusLike(currentDate,result,"출석",1);
               // 결석한 학생의 수
               int noCount = attendanceService.countByDateAndSchoolNameAndAttendanceStatusLikeAndStatusLike(currentDate,result,"결석",1);
               // 조퇴한 학생의 수
               int earlyCount = attendanceService.countByDateAndSchoolNameAndAttendanceStatusLikeAndStatusLike(currentDate,result,"조퇴",1);
               // 지각한 학생의 수
               int lateCount = attendanceService.countByDateAndSchoolNameAndAttendanceStatusLikeAndStatusLike(currentDate,result,"지각",1);

               model.addAttribute("okCount",okCount);
               model.addAttribute("outCount",outCount);
               model.addAttribute("safeCount",safeCount);
               model.addAttribute("noCount",noCount);
               model.addAttribute("earlyCount",earlyCount);
               model.addAttribute("lateCount",lateCount);
               return "main";
            }
         }

      } else {
         // login 실패
         System.out.println("login fail");
         return "login";
      }

   }




}
