package com.example.student.controller;

import com.example.student.repository.AttendanceRepository;
import com.example.student.service.AttendanceService;
import com.example.student.dto.AttendanceDTO;
import com.example.student.dto.DirectorDTO;
import com.example.student.entity.AttendanceEntity;
import com.example.student.entity.DirectorEntity;
import com.example.student.entity.ParentEntity;
import com.example.student.entity.TeacherEntity;
import com.example.student.service.DirectorService;
import com.example.student.service.MemberService;
import com.example.student.service.ParentService;
import com.example.student.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;


@Controller
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;
    private final ParentService parentService;
    private final MemberService memberService;
    private final AttendanceService attendanceService;
    private final TeacherService teacherService;
    private final AttendanceRepository attendanceRepository;

    // director역할 -> 유치원등록(반등록)
    @PostMapping("/director/save")
    public String saveDirector(@RequestParam Map<String, String> params,
                               @RequestParam(name = "classNames", required = false) String[] classNames,
                               @ModelAttribute DirectorDTO directorDTO, HttpSession session) {
        // 로그인한 사용자의 loginId 값 가져오기
        String loginId = (String) session.getAttribute("loginId");
        System.out.println("DirectorController.save");

        System.out.println("DirectorDTO=" + directorDTO);

        // DirectorDTO에서 필요한 데이터 가져오기
        String directorName = directorDTO.getDirectorName();
        String role = directorDTO.getRole();

        // user 테이블에 role 값 저장 (현재의 role정보를 loginId에 해당하는 걸 찾아서 role을 update해줌)
        memberService.updateMemberRole(loginId, role);

        String[] a = new String[classNames.length];
        String result = "";

        // classNames 배열이 존재한다면 처리
        if (classNames != null && classNames.length > 0) {
            directorService.save(directorDTO); // 기본 정보 먼저 저장

            for (String className : classNames) {
                a = className.split(",");
            }
            for (int i = 0; i < classNames.length; i++) {
                // System.out.println(a[i]);

                System.out.println("Class Name: " + a[i]);

                // System.out.println(a[i]);

                // DirectorEntity 생성 및 저장
                DirectorEntity directorEntity = new DirectorEntity();
                directorEntity.setClassName(a[i]);
                // 나머지 정보
                directorEntity.setLoginId(loginId);
                directorEntity.setDirectorName(directorName);
                directorEntity.setRole(role);
                directorEntity.setSchoolName(directorDTO.getSchoolName());
                directorEntity.setSchoolNumber(directorDTO.getSchoolNumber());
                directorEntity.setAddress(directorDTO.getAddress());
                directorEntity.setPostcode(directorDTO.getPostcode());
                directorEntity.setExtraAddress(directorDTO.getExtraAddress());
                directorEntity.setDetailAddress(directorDTO.getDetailAddress());

                // 저장
                directorService.save(DirectorDTO.toDirectorDTO(directorEntity));
            }

        }

        //나중에 원장님 권한 페이지로 바꾸기
        return "main";
    }


    //유치원 검색
    @GetMapping("/school/search")
    public String schoolSearch(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String selectedSchoolName, Model model) {
        //출력되는 모든값들이 중복제거되어서 출력돼야함
        if (keyword != null) {
            // 입력받은 keyword가 해당하는 SchoolName출력
            // Java 스트림을 사용하여 schoolName을 기준으로 중복된 값 제거
            List<DirectorEntity> directorEntityList = directorService.findBySchoolNameContainingOrAddressContainingOrDetailAddressContainingIgnoreCase(keyword);
            List<DirectorEntity> uniqueDirectorEntityList = directorEntityList.stream()
                    .collect(Collectors.toMap(DirectorEntity::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            model.addAttribute("memberList", uniqueDirectorEntityList);

        } else {
            List<DirectorDTO> directorDTOList = directorService.findSchool();

            // Java 스트림을 사용하여 schoolName을 기준으로 중복된 값 제거
            List<DirectorDTO> uniqueDirectorDTOList = directorDTOList.stream()
                    .collect(Collectors.toMap(DirectorDTO::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            model.addAttribute("memberList", directorDTOList);
        }

        return "selectSchool";
    }

    @PostMapping("/school/save")
    public String saveSchool(@RequestParam String selectedSchoolName,
                             @RequestParam String role, Model model) {
        // 어떠한 HTML로 가져갈 데이터가 있다면 model 사용
        // selectSchoolName
        // 원이름
        model.addAttribute("schoolName", selectedSchoolName);

        // selectBox에 반 정렬?
        List<DirectorEntity> directorEntities = directorService.findClassNamesBySchoolName(selectedSchoolName);
        model.addAttribute("className", directorEntities);

        //원 선택했을때 role에 따라 달라짐
        if ("학부모".equals(role)) {
            return "student";
        } else if ("선생님".equals(role)) {
            return "teacher";
        }
        return "index";
    }


    //원생관리 => main.html에서 원생 선택
    @PostMapping("/student/manager")
    public String studentManager(@ModelAttribute DirectorDTO directorDTO,
                                 @RequestParam String loginId, HttpSession session, Model model) {

        List<DirectorEntity> schoolName = directorService.findSchoolNameByLoginId(loginId);
        // schoolName 을 기준으로 중복제거 -> schoolName 한개만 출력
        List<DirectorEntity> schoolNameList = schoolName.stream()
                .collect(Collectors.toMap(DirectorEntity::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
        model.addAttribute("schoolName", schoolNameList);

        // schoolName을 기반으로 parent의 status 가져오기 list로
        String result = schoolNameList.get(0).getSchoolName();
        // selectBox에 반 정렬?
        List<DirectorEntity> classList = directorService.findClassNamesBySchoolName(result);
        model.addAttribute("classList2", classList);

        //승인 = 1 , 거부 = 2, 승인변경 = 1, 퇴소 = 3, 대기 =0

        // 행 수 구하기
        // status가 0인(승인대기) parent의 행의 수
        long studentWaitCount = parentService.countBySchoolNameAndStatusLike(result, 0);
        model.addAttribute("studentWaitCount", studentWaitCount);

        // status가 1인(승인완료) parent의 행의 수
        long studentOkCount = parentService.countBySchoolNameAndStatusLike(result, 1);
        model.addAttribute("studentOkCount", studentOkCount);

        // status가 2인(승인거부) parent의 행의 수
        long studentNoCount = parentService.countBySchoolNameAndStatusLike(result, 2);
        model.addAttribute("studentNoCount", studentNoCount);


        //list로 승인대기중 원생/ 승인완료 원생 출력
        List<ParentEntity> studentWaitList = parentService.findBySchoolNameAndStatusLike(result, 0);

        model.addAttribute("studentWaitList", studentWaitList);

        //list로 승인대기중 원생/ 승인완료 원생 출력
        List<ParentEntity> studentOkList = parentService.findBySchoolNameAndStatusLike(result, 1);

        model.addAttribute("studentOkList", studentOkList);

        //list로 승인대기중 원생/ 승인거부 원생 출력
        List<ParentEntity> studentNoList = parentService.findBySchoolNameAndStatusLike(result, 2);

        model.addAttribute("studentNoList", studentNoList);


        return "studentStatus";
    }


    // studentStatus 의 script 부분 => 승인/퇴소/거부 처리
    @PostMapping("/student/status")
    public @ResponseBody String studentStatus(@RequestParam("id") String id,
                                              @RequestParam("select") String select) {
        String checkResult = ""; // ajax 에 return 해줄 결과값

//        System.out.println("select"+select);
        if (id != null && !id.isEmpty()) {
            if ("승인".equals(select)) {
                int status = 1;
                parentService.updateStatus(Long.parseLong(id), status);
                //출석부 테이블(attendance)에 해당 학생의 정보 저장한다.(status=1인사람만 됨)
                List<ParentEntity> parents = parentService.findByIdAndStatusLike(Long.parseLong(id), status);

                checkResult = "ok";
            } else if ("거부".equals(select)) {
                int status = 2;
                parentService.updateStatus(Long.parseLong(id), status);
                attendanceService.updateStatus(Long.parseLong(id), status);
                checkResult = "no";
            } else if ("승인변경".equals(select)) {
                int status = 1;
                parentService.updateStatus(Long.parseLong(id), status);
                //출석부 테이블(attendance)에 해당 학생의 정보 저장한다.(status=1인사람만 됨)
                List<ParentEntity> parents = parentService.findByIdAndStatusLike(Long.parseLong(id), status);

                parentService.updateStatus(Long.parseLong(id), status);
                attendanceService.updateStatus(Long.parseLong(id), status);
                checkResult = "reOk";
            } else if ("퇴소".equals(select)) {
                int status = 3;
                //attendance 테이블의 status = 3으로 변경
                parentService.updateStatus(Long.parseLong(id), status);
                Long parentId = Long.parseLong(id);
                List<AttendanceEntity> attendanceList = attendanceRepository.findAllByParentId(parentId);
                if (!attendanceList.isEmpty()) {
                    for (AttendanceEntity attendance : attendanceList) {
                        attendance.setStatus(status);
                    }
                    attendanceRepository.saveAll(attendanceList);
                }
                checkResult = "out";
            } else {
                checkResult = "error";
            }
        } else {
            // ID가 비어 있을때
            checkResult = "error";
        }

        return checkResult;
    }

    // studentStatus 의 script 부분 => 반변경
    @PostMapping("/student/class")
    public @ResponseBody String studentClass(@RequestParam("id") String id,
                                             @RequestParam("className2") String className2,
                                             @RequestParam("select") String select) {
        String checkResult = ""; // ajax 에 return 해줄 결과값

        if (id != null && !id.isEmpty()) {
            if ("반변경".equals(select)) {
                // 반 update 하기 (가져온 className 으로)
                parentService.updateClassName(Long.parseLong(id), className2);

                // 출석부에 있는 반도 같이 바꾸기
                Long parentId = Long.parseLong(id);
                String className = className2;

                List<AttendanceEntity> attendanceList = attendanceRepository.findAllByParentId(parentId);
                if (!attendanceList.isEmpty()) {
                    for (AttendanceEntity attendance : attendanceList) {
                        attendance.setClassName(className);
                    }
                    attendanceRepository.saveAll(attendanceList);
                }
                checkResult = "reClass";
            } else {
                checkResult = "error";
            }
        } else {
            // ID가 비어 있을 때
            checkResult = "error";
        }

        return checkResult;
    }

    @PostMapping("/teacher/class")
    public @ResponseBody String teacherClass(@RequestParam("id") String id,
                                             @RequestParam("className2") String className2,
                                             @RequestParam("select") String select) {
        String checkResult = ""; // ajax 에 return 해줄 결과값
//        System.out.println("className"+className);
//        System.out.println("select"+select);
        if (id != null && !id.isEmpty()) {
            if ("반변경".equals(select)) {
                //반 update하기 (가져온 className으로)
                teacherService.updateClassName(Long.parseLong(id), className2);
                checkResult = "reClass";
            } else {
                checkResult = "error";
            }
        } else {
            // ID가 비어 있을때
            checkResult = "error";
        }

        return checkResult;
    }

    //교사관리 => main.html 에서 원생 선택
    @PostMapping("/teacher/manager")
    public String teacherManager(@ModelAttribute DirectorDTO directorDTO,
                                 @RequestParam String loginId, HttpSession session, Model model) {

        List<DirectorEntity> schoolName = directorService.findSchoolNameByLoginId(loginId);
        // schoolName 을 기준으로 중복제거 -> schoolName 한개만 출력
        List<DirectorEntity> schoolNameList = schoolName.stream()
                .collect(Collectors.toMap(DirectorEntity::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
        model.addAttribute("schoolName", schoolNameList);

        // schoolName을 기반으로 parent의 status 가져오기 list로
        String result = schoolNameList.get(0).getSchoolName();
        // selectBox에 반 정렬?
        List<DirectorEntity> classList = directorService.findClassNamesBySchoolName(result);
        model.addAttribute("classList1", classList);

        //승인 = 1 , 거부 = 2, 승인변경 = 1, 퇴소 = 3, 대기 =0

        // 행 수 구하기
        // status가 0인(승인대기) teacher 행의 수
        long teacherWaitCount = teacherService.countBySchoolNameAndStatusLike(result, 0);
        model.addAttribute("teacherWaitCount", teacherWaitCount);

        // status가 1인(승인완료) teacher 행의 수
        long teacherOkCount = teacherService.countBySchoolNameAndStatusLike(result, 1);
        model.addAttribute("teacherOkCount", teacherOkCount);

        // status가 2인(승인거부) teacher 행의 수
        long teacherNoCount = teacherService.countBySchoolNameAndStatusLike(result, 2);
        model.addAttribute("teacherNoCount", teacherNoCount);


        //list로 승인대기중 원생/ 승인완료 교사 출력
        List<TeacherEntity> teacherWaitList = teacherService.findBySchoolNameAndStatusLike(result, 0);

        model.addAttribute("teacherWaitList", teacherWaitList);

        //list로 승인대기중 원생/ 승인완료 교사 출력
        List<TeacherEntity> teacherOkList = teacherService.findBySchoolNameAndStatusLike(result, 1);

        model.addAttribute("teacherOkList", teacherOkList);

        //list로 승인대기중 원생/ 승인거부 교사 출력
        List<TeacherEntity> teacherNoList = teacherService.findBySchoolNameAndStatusLike(result, 2);

        model.addAttribute("teacherNoList", teacherNoList);


        return "teacherStatus";
    }


    // teacherStatus 의 script 부분 => 승인/퇴소/거부 처리
    @PostMapping("/teacher/status")
    public @ResponseBody String teacherStatus(@RequestParam("id") String id,

                                              @RequestParam("select") String select) {
        String checkResult = ""; // ajax 에 return 해줄 결과값
//        System.out.println("className"+className);
//        System.out.println("select"+select);
        if (id != null && !id.isEmpty()) {
            if ("승인".equals(select)) {
                int status = 1;
                teacherService.updateStatus(Long.parseLong(id), status);
                checkResult = "ok";
            } else if ("거부".equals(select)) {
                int status = 2;
                teacherService.updateStatus(Long.parseLong(id), status);
                checkResult = "no";
            } else if ("승인변경".equals(select)) {
                int status = 1;
                teacherService.updateStatus(Long.parseLong(id), status);
                checkResult = "reOk";
            } else if ("퇴소".equals(select)) {
                int status = 3;
                teacherService.updateStatus(Long.parseLong(id), status);
                checkResult = "out";
            } else {
                checkResult = "error";
            }
        } else {
            // ID가 비어 있을때
            checkResult = "error";
        }

        return checkResult;
    }


    // main.html -> schoolSetting.html  => 원 설정페이지로 이동
    @PostMapping("/school/set")
    public String setting(Model model,HttpSession session){
        String loginId = (String) session.getAttribute("loginId");

        //혀재 로그인된 사용자를 기반으로 director 테이블에 있는 studentInTime, studentOutTime 가져오기
        List<DirectorEntity> directorList = directorService.findByLoginId(loginId);
        List<DirectorEntity> schoolTimeList = directorList.stream()
                .collect(Collectors.toMap(DirectorEntity::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
        String schoolName = schoolTimeList.get(0).getSchoolName();
        model.addAttribute("schoolTimeList", schoolTimeList);
        model.addAttribute("directorList",directorList);

        // 현재 해당 유치원의 반 이름 모두를 리스트로 저장
        List<DirectorEntity> classList = directorService.findClassNamesBySchoolName(schoolName);
        model.addAttribute("classList2", classList);

        return "schoolSetting";
    }

    //schoolSetting.html 에서 등하원 시간 수정버튼 누르면 jquery 로 값받아와서 실행
    @PostMapping("/school/time/update")
    public @ResponseBody String updateTime(@RequestParam("inTime") String inTime,
                                           @RequestParam("outTime") String outTime,HttpSession session) {

        String loginId = (String) session.getAttribute("loginId");

        // 받아온 inTime 값과 outTime 값 int 형을 바꾸기
        System.out.println(inTime);
        System.out.println(outTime);
        // 받아온 inTime 값을 :으로 나누고 int 형으로 변환
        String[] inTimeParts = inTime.split(":");
        int inHour = Integer.parseInt(inTimeParts[0]);

        String[] inOutParts = outTime.split(":");
        int outHour = Integer.parseInt(inOutParts[0]);

        // String -> LocalTime 으로 형변환
        LocalTime inTimeLocal = LocalTime.parse(inTime);
        LocalTime outTimeLocal = LocalTime.parse(outTime);

        // inHour 보다 outHour 이 작아서는 안된다.
        if(inHour >= outHour){
            System.out.println("No");
            return "no";
        }else if(inHour < outHour){
            System.out.println("Ok");
            //해당 loginId 에 해당하는 director 테이블의 컬럼에(studentInTime/studentOutTime) 저장
            directorService.updateTime(loginId,inTimeLocal,outTimeLocal);
            return "ok";
        }else{
            return "error";
        }
    }

    //schoolSetting.html 에서 출퇴근 시간 수정버튼 누르면 jquery 로 값받아와서 실행
    @PostMapping("/school/teacher/time/update")
    public @ResponseBody String updateTeacherTime(@RequestParam("inTime") String inTime,
                                           @RequestParam("outTime") String outTime,HttpSession session) {

        String loginId = (String) session.getAttribute("loginId");

        // 받아온 inTime 값과 outTime 값 int 형을 바꾸기
        System.out.println(inTime);
        System.out.println(outTime);
        // 받아온 inTime 값을 :으로 나누고 int 형으로 변환
        String[] inTimeParts = inTime.split(":");
        int inHour = Integer.parseInt(inTimeParts[0]);

        String[] inOutParts = outTime.split(":");
        int outHour = Integer.parseInt(inOutParts[0]);

        // String -> LocalTime 으로 형변환
        LocalTime inTimeLocal = LocalTime.parse(inTime);
        LocalTime outTimeLocal = LocalTime.parse(outTime);
        //해당 loginId 에 해당하는 director 테이블의 컬럼에(studentInTime/studentOutTime) 저장
        directorService.updateTeacherTime(loginId,inTimeLocal,outTimeLocal);
        return "ok";

    }

    // 반 추가 jquery
    @PostMapping("/school/add/class")
    public @ResponseBody String addClass(@RequestParam("addClassName") String addClassName,HttpSession session) {

        String loginId = (String) session.getAttribute("loginId");

        // 받아온 inTime 값과 outTime 값 int 형을 바꾸기
        System.out.println(addClassName);

        //혀재 로그인된 사용자를 기반으로 director 테이블
        List<DirectorEntity> directorList = directorService.findByLoginId(loginId);
        List<DirectorEntity> directorEntityList = directorList.stream()
                .collect(Collectors.toMap(DirectorEntity::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());

        // 직접 db에 값 넣어주기
        for (DirectorEntity addClass : directorEntityList) {
            DirectorEntity directorEntity = new DirectorEntity();
            directorEntity.setSchoolNumber(addClass.getSchoolNumber());
            directorEntity.setAddress(addClass.getAddress());
            directorEntity.setClassName(addClassName); // 추가로 받아온 className
            directorEntity.setDetailAddress(addClass.getDetailAddress());
            directorEntity.setDirectorName(addClass.getDirectorName());
            directorEntity.setExtraAddress(addClass.getExtraAddress());
            directorEntity.setLoginId(addClass.getLoginId());
            directorEntity.setPostcode(addClass.getPostcode());
            directorEntity.setRole(addClass.getRole());
            directorEntity.setSchoolName(addClass.getSchoolName());
            directorEntity.setStudentInTime(addClass.getStudentInTime());
            directorEntity.setStudentOutTime(addClass.getStudentOutTime());
            directorEntity.setTeacherInTime(addClass.getTeacherInTime());
            directorEntity.setTeacherOutTime(addClass.getTeacherOutTime());

            directorService.save(DirectorDTO.toDirectorDTO(directorEntity));
        }
        return "ok";
    }


    // 반 삭제 jquery
    @PostMapping("/school/delete/class")
    public @ResponseBody String deleteClass(@RequestParam("deleteClass") String deleteClass,HttpSession session) {

        String loginId = (String) session.getAttribute("loginId");

        // 받아온 inTime 값과 outTime 값 int 형을 바꾸기
        System.out.println(deleteClass);
        // 반 삭제하면 director 테이블에 해당 className 과 일치하는 행 삭제하고 parent 와 attendance
        // 테이블의 해당되는 className 을 가진 행의 status=3으로 바꾼
        // 1) 로그인된 사용자의 schoolName 을 찾아오기
        List<DirectorEntity> directorList = directorService.findByLoginId(loginId);
        List<DirectorEntity> directorEntityList = directorList.stream()
                .collect(toMap(DirectorEntity::getSchoolName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
        String schoolName = directorEntityList.get(0).getSchoolName(); // 원 이름

        System.out.println("schoolName"+schoolName);
        // 2) director 테이블에서 loginId 의 schoolName 과 className 에 일치하는 사용자 찾아서 삭제


        // director 테이블에서 해당되는 반과 schoolName 과 일치하는 id 를 찾아내기
        Optional<DirectorEntity> directorId = directorService.findIdBySchoolNameAndClassName(schoolName, deleteClass);
        Long id = directorId.get().getId();
        directorService.deleteById(id);

        // 3) parent 테이블에서 schoolName 과 className 이 일치하는 사용자 status = 3 으로 변경한다.
        // schoolName 과 className 이 일치하는 parent 테이블의 id 구하기
        List<ParentEntity> parentId = parentService.findBySchoolNameAndClassName(schoolName, deleteClass);
        int countId = parentService.countBySchoolNameAndClassName(schoolName, deleteClass);
        int countAId = attendanceService.countBySchoolNameAndClassName(schoolName, deleteClass);
        System.out.println("countId:"+countId);
        System.out.println("countAId:"+countAId);
        if (!parentId.isEmpty()) {
            int status =3;
            // prentId 가 출력되는 행의 수 만큼
            for(int i=0; i<countId; i++){
                Long pId = parentId.get(i).getId();
                System.out.println("pId:"+pId);
                parentService.updateStatus(pId,status);
            }
            if(countAId !=0){
                for(int i=0; i<countAId; i++){
                    Long pId = parentId.get(i).getId();
                    attendanceService.updateStatus(pId,status);
                }
            }
        }

        return "ok";
    }
}



