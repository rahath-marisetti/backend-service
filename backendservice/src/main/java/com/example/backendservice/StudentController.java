package com.example.backendservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping("/newstudent")
    public Student createstudent(@RequestBody Student s) {
        return studentService.newStudent(s);

    }

    @GetMapping("/getid")
    public Student fetchbyid(@RequestParam int id) {
        return studentService.fetchbyid(id);

    }

}
