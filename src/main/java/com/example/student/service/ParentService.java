package com.example.student.service;

import com.example.student.dto.ParentDTO;
import com.example.student.entity.AttendanceEntity;
import com.example.student.entity.ParentEntity;
import com.example.student.repository.AttendanceRepository;
import com.example.student.repository.DirectorRepository;
import com.example.student.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParentService {
    private final ParentRepository parentRepository;
    private final AttendanceRepository attendanceRepository;
    private final DirectorRepository directorRepository;
    //parent 테이블에 html에서 입력한 값 저장
    public void save(ParentDTO parentDTO) {
        ParentEntity parentEntity = ParentEntity.toParentEntity(parentDTO);
        parentRepository.save(parentEntity); //save() -> respositroy에서 save메서드 지원함
    }

    // parent 의 schoolName=?면서 STATUS=?인 행의 수
    public long countBySchoolNameAndStatusLike(String schoolName,int status) {
        return parentRepository.countBySchoolNameAndStatusLike(schoolName,status);
    }


    public List<ParentEntity> findBySchoolNameAndStatusLike(String schoolName,int status) {
        return parentRepository.findBySchoolNameAndStatusLike(schoolName,status);
    }


    public void approveStudent(Long parentId) {
        Optional<ParentEntity> optionalParentEntity = parentRepository.findById(parentId);

        // Check if the parent entity exists
        if (optionalParentEntity.isPresent()) {
            // Update the status to 1 for approval
            ParentEntity parentEntity = optionalParentEntity.get();
            parentEntity.setStatus(1);
            parentRepository.save(parentEntity);
        } else {
            throw new RuntimeException("Parent not found with id: " + parentId);
        }
    }

    // id 기반으로 status update
    public void updateStatus(long id, int status){
        Optional<ParentEntity> optionalParent = parentRepository.findById(id);
        optionalParent.ifPresent(parent->{
            parent.setStatus(status);
            parentRepository.save(parent);
        });
    }


    //className update 해당
    public void updateClassName(long id, String className){
        Optional<ParentEntity> optionalParent = parentRepository.findById(id);
        optionalParent.ifPresent(parent->{
            parent.setClassName(className);
            parentRepository.save(parent);
        });
    }

    public List<ParentEntity> findByIdAndStatusLike(long id, int status) {
        return parentRepository.findByIdAndStatusLike(id,status);
    }

    public List<ParentEntity> findBySchoolNameAndClassNameAndStatusLike(String schoolName, String className, int status) {
        return parentRepository.findBySchoolNameAndClassNameAndStatusLike(schoolName, className, status);
    }

    public Optional<ParentEntity> findById(Long id) {
        return parentRepository.findById(id);
    }

    public Optional<ParentEntity> findSchoolNameByLoginId(String loginId) {
        return parentRepository.findSchoolNameByLoginId(loginId);
    }

    public Optional<ParentEntity> findClassNameByLoginId(String loginId) {
        return parentRepository.findClassNameByLoginId(loginId);
    }

    public Optional<ParentEntity> findIdByLoginId(String loginId) {
        return parentRepository.findIdByLoginId(loginId);
    }

    public List<ParentEntity> findBySchoolNameAndClassName(String schoolName, String className) {
        return parentRepository.findBySchoolNameAndClassName(schoolName,className);
    }

    public Optional<ParentEntity> findIdBySchoolNameAndClassName(String schoolName, String className) {
        return parentRepository.findIdBySchoolNameAndClassName(schoolName,className);
    }

    public int countBySchoolNameAndClassName(String schoolName, String className) {
        return parentRepository.countBySchoolNameAndClassName(schoolName,className);
    }
}
