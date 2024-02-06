package com.example.student.service;

import com.example.student.dto.DirectorDTO;
import com.example.student.entity.DirectorEntity;
import com.example.student.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    // 저장할떄 등하원 / 출퇴근 시간 기본값으로 저장
    public void save(DirectorDTO directorDTO){
        DirectorEntity directorEntity = DirectorEntity.toDirectorEntity(directorDTO);
        directorEntity.setStudentInTime(LocalTime.parse("09:00"));
        directorEntity.setStudentOutTime(LocalTime.parse("18:00"));
        directorEntity.setTeacherInTime(LocalTime.parse("08:30"));
        directorEntity.setTeacherOutTime(LocalTime.parse("18:30"));
        directorRepository.save(directorEntity);
    }

    public List<DirectorDTO> findSchool() {
        //alt+enter => 단축키
        List<DirectorEntity> directorEntityList = directorRepository.findAll();//findAll -> repository가 지원하는 메서드
        //repository와 연결된 객체는 무조건 entity와 연결
        //entity-> dto 변환해줘야함
        //entity가 여러개 담긴 객체를 dto가 여러개 담긴 객체로 옮겨준다.
        List<DirectorDTO> directorDTOList = new ArrayList<>();
        //하나하나씩 꺼내서 배열에 담아준다
        for(DirectorEntity directorEntity : directorEntityList){
            directorDTOList.add(DirectorDTO.toDirectorDTO(directorEntity));
        }
        return directorDTOList;
    }

    public List<DirectorEntity> findClassNamesBySchoolName(String schoolName) {
        return directorRepository.findClassNamesBySchoolName(schoolName);
    }


    public List<DirectorEntity> findBySchoolNameContainingOrAddressContainingOrDetailAddressContainingIgnoreCase(String keyword) {
        return directorRepository.findBySchoolNameContainingOrAddressContainingOrDetailAddressContainingIgnoreCase(keyword,keyword,keyword);

    }

    public List<DirectorEntity> findSchoolNameByLoginId(String loginId){
        return directorRepository.findSchoolNamesByLoginId(loginId);
    }


    public List<DirectorEntity> findByLoginId(String loginId) {
        return directorRepository.findByLoginId(loginId);
    }

    // 원설정 페이지에서 유치원 등하원 시간 설정 => 등하원 시간 update
    public void updateTime(String loginId, LocalTime studentInTime, LocalTime studentOutTime) {
        List<DirectorEntity> directorEntityList = directorRepository.findByLoginId(loginId);
        for (DirectorEntity directorEntity : directorEntityList) {
            //director 테이블에 각각의 값 저장
            directorEntity.setStudentInTime(studentInTime);
            directorEntity.setStudentOutTime(studentOutTime);

            directorRepository.save(directorEntity);
        }

    }
    // 원설정 페이지에서 유치원 출퇴근 시간 설정 => 출퇴근 시간 update
    public void updateTeacherTime(String loginId, LocalTime teacherInTime, LocalTime teacherOutTime) {
        List<DirectorEntity> directorEntityList = directorRepository.findByLoginId(loginId);
        for (DirectorEntity directorEntity : directorEntityList) {
            //director 테이블에 각각의 값 저장
            directorEntity.setTeacherInTime(teacherInTime);
            directorEntity.setTeacherOutTime(teacherOutTime);

            directorRepository.save(directorEntity);
        }

    }

    public List<DirectorEntity> findBySchoolNameAndClassName(String schoolName, String className) {
        return directorRepository.findBySchoolNameAndClassName(schoolName,className);
    }

    public List<DirectorEntity> findTeacherInTimeAndTeacherOutTimeBySchoolNameAndClassName(String schoolName, String className) {
        return directorRepository.findTeacherInTimeAndTeacherOutTimeBySchoolNameAndClassName(schoolName,className);
    }


    public List<DirectorEntity> findStudentInTimeAndStudentOutTimeBySchoolNameAndClassName(String schoolName, String className) {
        return directorRepository.findStudentInTimeAndStudentOutTimeBySchoolNameAndClassName(schoolName,className);
    }


    public void deleteByLoginIdAndSchoolNameAndClassName(String loginId, String schoolName, String className) {
         directorRepository.deleteByLoginIdAndSchoolNameAndClassName(loginId, schoolName, className);
    }

    public void deleteBySchoolNameAndClassName(String schoolName, String className) {
         directorRepository.deleteBySchoolNameAndClassName(schoolName,className);
    }

    public Optional<DirectorEntity> findIdBySchoolNameAndClassName(String schoolName, String className) {
        return directorRepository.findIdBySchoolNameAndClassName(schoolName,className);
    }

    public void deleteById(Long id) {
        directorRepository.deleteById(id);
    }
}
