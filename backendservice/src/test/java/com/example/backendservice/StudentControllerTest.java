package com.example.backendservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setStudentId(1);
        student.setName("Alice");
        student.setEmail("alice@example.com");
        student.setRollNumber(101);
    }

    // --- GET /hello ---

    @Test
    void hello_returnsHelloWorld() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    // --- POST /newstudent ---

    @Test
    void createStudent_returnsCreatedStudent() throws Exception {
        when(studentService.newStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/newstudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.rollNumber").value(101));
    }

    // --- GET /getid ---

    @Test
    void fetchById_whenStudentExists_returnsStudent() throws Exception {
        when(studentService.fetchbyid(1)).thenReturn(student);

        mockMvc.perform(get("/getid").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void fetchById_whenStudentDoesNotExist_returnsNull() throws Exception {
        when(studentService.fetchbyid(999)).thenReturn(null);

        mockMvc.perform(get("/getid").param("id", "999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
