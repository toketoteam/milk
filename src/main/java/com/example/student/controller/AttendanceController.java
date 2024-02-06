package com.example.student.controller;

import com.example.student.dto.AttendanceDTO;
import com.example.student.dto.MemberDTO;
import com.example.student.dto.ParentDTO;
import com.example.student.dto.TeacherDTO;
import com.example.student.entity.AttendanceEntity;
import com.example.student.entity.DirectorEntity;
import com.example.student.entity.ParentEntity;
import com.example.student.entity.TeacherEntity;
import com.example.student.repository.DirectorRepository;
import com.example.student.service.AttendanceService;
import com.example.student.service.DirectorService;
import com.example.student.service.ParentService;
import com.example.student.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AttendanceController {

    private final TeacherService teacherService;
    private final AttendanceService attendanceService;
    private final ParentService parentService;

    private final DirectorService directorService;
    @PostMapping("/student/attendance")
    public String attendanceList(@ModelAttribute TeacherDTO teacherDTO, HttpSession session, Model model){
        String loginId = (String) session.getAttribute("loginId");

        if(loginId != null){
            // 세션에서 가져온 loginId를 기반으로 teacher 정보 가져오기
            TeacherEntity teacher = teacherService.getTeacherByLoginId(loginId);
            LocalDate currentDate = LocalDate.now();
            if (teacher != null) {
                // 현재로그인한 선생님의 반 이름이랑 원 이름 가져오기
                String className = teacher.getClassName();
                String schoolName = teacher.getSchoolName();
                model.addAttribute("className",className);  // 반 이름
                model.addAttribute("schoolName",schoolName);  // 원 이름

                //inTime 과 outTime 찾기
                List<DirectorEntity> directorList = directorService.findBySchoolNameAndClassName(schoolName,className);
                model.addAttribute("directorList",directorList);

//                // teacherTable에서 가져온 정보를 기반으로 attendance 정보 가져오기 + status 가 1인 학생
                List<AttendanceEntity> attendanceList = attendanceService.findBySchoolNameAndClassNameAndStatusLikeAndDateLike(schoolName,className,1,currentDate);
                model.addAttribute("attendanceList",attendanceList);
                List<ParentEntity> studentList = parentService.findBySchoolNameAndClassNameAndStatusLike(schoolName, className, 1);
                model.addAttribute("studentList", studentList);
            } else {
                // teacher가 없는 경우에 대한 처리
                model.addAttribute("errorMessage", "teacherEntity를 찾을 수 없습니다.");
            }
        } else {
            // 로그인이 안 된 경우에 대한 처리
            model.addAttribute("errorMessage", "로그인이 되어있지 않은 상태 입니다.");
        }

        return "studentAttendance";
    }

    @PostMapping("/attendance/status/in")
    public ResponseEntity<String> updateIn(@RequestParam("id") Long id,Model model,
                                           @RequestParam("schoolInTime") LocalTime schoolInTime
                                            ,@RequestParam("schoolOutTime") LocalTime schoolOutTime) {

        // attendance 테이블에 해당 id의 현재날짜 의 inTime 과 id가 없으면
        // 받아온 id에 해당하는 parent 테이블 정보를 가져옴
        //Optional<AttendanceEntity> idCheck = attendanceService.findIdById(id);
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        System.out.println(schoolInTime+"~"+schoolOutTime);
        // 해당 id(parentId)의 현재날짜의 inTime의 존재여부
        Optional<AttendanceEntity> inTimeCheck = attendanceService.findInTimeByParentIdAndDateLike(id,currentDate);
        // 해당 유치원의 등하원 시간
        System.out.println("inTimeCheck"+inTimeCheck);
        //출석부 테이블(attendance)에 해당 학생의 정보 저장한다.(status=1인사람만 됨)
        List<ParentEntity> parents = parentService.findByIdAndStatusLike(id, 1);
        if(!inTimeCheck.isPresent()){

            for (ParentEntity parent : parents) {
                AttendanceEntity attendanceEntity = new AttendanceEntity();

                // 부모 정보에서 가져와서 AttendanceEntity에 설정
                attendanceEntity.setParentId(parent.getId());
                attendanceEntity.setStudentName(parent.getStudentName());
                attendanceEntity.setClassName(parent.getClassName());
                attendanceEntity.setStatus(parent.getStatus());
                attendanceEntity.setSchoolName(parent.getSchoolName());
                // 비교해서 등원 또는 지각 여부 결정
                String attendanceStatus = timeCheck(currentTime, schoolInTime);
                attendanceEntity.setAttendanceStatus(attendanceStatus); //비교해서 return 값 보내기
                attendanceEntity.setDate(currentDate); // 현재날짜
                attendanceEntity.setInTime(currentTime); // 현재시간
                attendanceService.save(AttendanceDTO.toAttendanceDTO(attendanceEntity));
            }
            String result =  timeCheck(currentTime, schoolInTime);
            return ResponseEntity.ok(result);
        }else {
            // null 값이 아닐경우에는 update 해준다.
            attendanceService.updateInTime(id,currentDate,currentTime);
            return ResponseEntity.ok("update");
        }
    }

    private String timeCheck(LocalTime currentTime, LocalTime schoolInTime) {
        if (currentTime.isAfter(schoolInTime)) {
            return "등원(지각)";
        } else {
            return "등원";
        }
    }

    // 하원 처리 => outTime 저장
    @PostMapping("/attendance/status/out")
    public @ResponseBody String updateOut(@ModelAttribute AttendanceDTO attendanceDTO, @RequestParam("id") Long id
            , @RequestParam("inTimeValue") String inTimeValue, @RequestParam("outTimeValue") String outTimeValue) {

        // attendance 테이블에서 id를 기반으로 일치하는거 가져와서
        // inTime / outTime 값 가져오기
        LocalDate currentDate = LocalDate.now(); //현재날짜 가져오기
        Optional<AttendanceEntity> inT = attendanceService.findInTimeByParentIdAndDateLike(id,currentDate);
        LocalTime studentInTime = inT.get().getInTime();
        LocalTime currentTime = LocalTime.now(); //현재시간 가져오기
        System.out.println("studentInTime"+studentInTime);
        String attendanceStatus = "";
        System.out.println("Time:" + studentInTime+"~"+currentTime);

        // 고정 등원시간 = inTimeValue  고정 하원시간 = outTimeValue
        // 학생 등원시간 = studentInTime 학생 하원시간 = currentTime
        // 등원 처리가 되어야 하원처리가 정상적으로 된다.
        if (studentInTime != null) {
            // 만약 고정등원시간 < 학생등원시간 이면서 고정하원시간 > 학생하원시간 이면 지각
            if (studentInTime.isAfter(LocalTime.parse(inTimeValue)) && (currentTime.isAfter(LocalTime.parse(outTimeValue)) || currentTime.equals(LocalTime.parse(outTimeValue)))) {
                System.out.println("late");
                attendanceStatus = "지각";
                attendanceService.updateOutTime(id, currentTime,attendanceStatus,currentDate);
                return "지각";
            }else if (LocalTime.parse(inTimeValue).isAfter(studentInTime) && (currentTime.isAfter(LocalTime.parse(outTimeValue)) || currentTime.equals(LocalTime.parse(outTimeValue)))||LocalTime.parse( inTimeValue).equals(studentInTime)) {
                System.out.println("attend");
                attendanceStatus = "출석";
                attendanceService.updateOutTime(id, currentTime,attendanceStatus,currentDate);
                return "출석";
            }else if (LocalTime.parse(inTimeValue).isAfter(studentInTime) && (LocalTime.parse(outTimeValue).isAfter(currentTime) )||LocalTime.parse( inTimeValue).equals(studentInTime)) {
                System.out.println("early");
                attendanceStatus = "조퇴";
                attendanceService.updateOutTime(id, currentTime,attendanceStatus,currentDate);
                return "조퇴";
            }else{
                attendanceStatus = "결석";
                attendanceService.updateOutTime(id, currentTime,attendanceStatus,currentDate);
                return "결석";
            }
            // attendance 테이블 업데이트
        } else if (studentInTime == null) {
            System.out.println("오류");
            return "no";
        }

        return "ok";
    }


    // 결석 처리 => StatusTime 저장
    @PostMapping("/attendance/status/no")
    public @ResponseBody String updateNo(@RequestParam("id") Long id) {
        LocalDate currentDate = LocalDate.now(); //현재날짜 가져오기
        String status = "결석";
        String attendanceStatus = "결석";

        // 해당 id(parentId)의 현재날짜의 inTime의 존재여부
        Optional<AttendanceEntity> inTimeCheck = attendanceService.findInTimeByParentIdAndDateLike(id,currentDate);
        // 해당 유치원의 등하원 시간
        System.out.println("inTimeCheck"+inTimeCheck);
        //출석부 테이블(attendance)에 해당 학생의 정보 저장한다.(status=1인사람만 됨)
        List<ParentEntity> parents = parentService.findByIdAndStatusLike(id, 1);
        //만약에 db에 현재 날짜의 행이 존재하지 않는다면 직접추가
        if(!inTimeCheck.isPresent()) {

            for (ParentEntity parent : parents) {
                AttendanceEntity attendanceEntity = new AttendanceEntity();

                // 부모 정보에서 가져와서 AttendanceEntity에 설정
                attendanceEntity.setParentId(parent.getId());
                attendanceEntity.setStudentName(parent.getStudentName());
                attendanceEntity.setClassName(parent.getClassName());
                attendanceEntity.setStatus(parent.getStatus());
                attendanceEntity.setSchoolName(parent.getSchoolName());

                attendanceEntity.setAttendanceStatus("결석"); //비교해서 return 값 보내기
                attendanceEntity.setDate(currentDate); // 현재날짜

                attendanceService.save(AttendanceDTO.toAttendanceDTO(attendanceEntity));
            }
            return "ok";
        }
        else{
            // attendance 테이블 inTime 업데이트
            attendanceService.updateAttendanceStatus(id, attendanceStatus,currentDate);
            return "ok";
        }

    }


    @PostMapping("/student/attendance/all")
    public String attendanceAllList(@ModelAttribute TeacherDTO teacherDTO, HttpSession session, Model model){
        String loginId = (String) session.getAttribute("loginId");

        if(loginId != null){
            LocalDate currentDate = LocalDate.now(); //현재 날짜
            // 세션에서 가져온 loginId를 기반으로 director 정보 가져오기
            List<DirectorEntity> director = directorService.findByLoginId(loginId);

            List<DirectorEntity> directorList = director.stream()
                    .collect(Collectors.toMap(DirectorEntity::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            String schoolName = null; // schoolName을 담을 변수를 초기화합니다. 기본값은 null이나 필요에 따라 다른 값을 지정할 수 있습니다.

            // directorList가 비어있지 않은 경우에만 수행합니다.
            if (!directorList.isEmpty()) {
                // directorList에서 첫 번째 항목의 schoolName을 가져옵니다.
                schoolName = directorList.get(0).getSchoolName();
                model.addAttribute("schoolName",schoolName);
                LocalTime studentInTime = directorList.get(0).getStudentInTime();
                model.addAttribute("studentInTime",studentInTime);
                LocalTime studentOutTime = directorList.get(0).getStudentOutTime();
                model.addAttribute("studentOutTime",studentOutTime);

                List<ParentEntity> studentList = parentService.findBySchoolNameAndStatusLike(schoolName,1);
                model.addAttribute("studentList", studentList);

                // attendance 테이블에서 가져온 정보를 기반으로 attendance 정보 가져오기 + status 가 1인 학생
                List<AttendanceEntity> attendanceList = attendanceService.findBySchoolNameAndStatusLikeAndDateLike(schoolName,1,currentDate);
                model.addAttribute("attendanceList",attendanceList);

                // 현재 해당 유치원의 반 이름 모두를 리스트로 저장
                List<DirectorEntity> classList = directorService.findClassNamesBySchoolName(schoolName);
                model.addAttribute("classList2", classList);

            }
           else {
                // director 없는 경우에 대한 처리
                model.addAttribute("errorMessage", "directorEntity 를 찾을 수 없습니다.");
            }
        } else {
            // 로그인이 안 된 경우에 대한 처리
            model.addAttribute("errorMessage", "로그인이 되어있지 않은 상태 입니다.");
        }

        return "studentAttendanceAll";
    }


}
