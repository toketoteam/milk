package com.example.student.service;

import com.example.student.dto.TeacherDTO;
import com.example.student.entity.DirectorEntity;
import com.example.student.entity.ParentEntity;
import com.example.student.entity.TeacherEntity;
import com.example.student.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;


    public void save(TeacherDTO teacherDTO){
        TeacherEntity teacherEntity = TeacherEntity.toTeacherEntity(teacherDTO);
        teacherRepository.save(teacherEntity);
    }

    // teacher 의 schoolName=?면서 STATUS=?인 행의 수
    public long countBySchoolNameAndStatusLike(String schoolName,int status) {
        return teacherRepository.countBySchoolNameAndStatusLike(schoolName,status);
    }

    public void updateStatus(long id, int status){
        Optional<TeacherEntity> optionalParent = teacherRepository.findById(id);
        optionalParent.ifPresent(teacher->{
            teacher.setStatus(status);
            teacherRepository.save(teacher);
        });
    }

    //className update 해당
    public void updateClassName(long id, String className){
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);
        optionalTeacher.ifPresent(teacher->{
            teacher.setClassName(className);
            teacherRepository.save(teacher);
        });
    }

    public List<TeacherEntity> findBySchoolNameAndStatusLike(String schoolName, int status) {
        return teacherRepository.findBySchoolNameAndStatusLike(schoolName,status);
    }

    public TeacherEntity getTeacherByLoginId(String loginId){
        return teacherRepository.findByLoginId(loginId);
    }


    public List<TeacherEntity> findSchoolNameAndClassNameByLoginId(String loginId) {
        return  teacherRepository.findSchoolNameAndClassNameByLoginId(loginId);
    }


    public Optional<TeacherEntity> findSchoolNameByLoginId(String loginId) {
        return teacherRepository.findSchoolNameByLoginId(loginId);
    }

    public Optional<TeacherEntity> findClassNameByLoginId(String loginId) {
        return teacherRepository.findClassNameByLoginId(loginId);
    }


}
