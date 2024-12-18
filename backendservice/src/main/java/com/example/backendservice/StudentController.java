package com.example.backendservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/grantcode")
    public String grantCode(@RequestParam("code") String code, @RequestParam("scope") String scope, @RequestParam("authuser") String authUser, @RequestParam("prompt") String prompt) {
        return processGrantCode(code);
    }

    @PostMapping("/newstudent")
    public Student createstudent(@RequestBody Student s) {
        return studentService.newStudent(s);

    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/getid")
    public Student fetchbyid(@RequestParam int id) {
        return studentService.fetchbyid(id);

    }

}
