package modules.services;

import modules.entities.StudentEntity;
import modules.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public ArrayList<StudentEntity> getStudents() {
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }



}
