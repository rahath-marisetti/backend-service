package com.example.backendservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public Student newStudent (Student student){
        return studentRepository.save(student);
    }
    public Student fetchbyid(int id) {
        Optional<Student> studentEntity = studentRepository.findById(id);
        if (studentEntity.isPresent()) {
            return studentEntity.get();
        } else {
            return null;

        }

    }


}
