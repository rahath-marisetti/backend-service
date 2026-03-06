package com.example.backendservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setStudentId(1);
        student.setName("Alice");
        student.setEmail("alice@example.com");
        student.setRollNumber(101);
    }

    // --- newStudent ---

    @Test
    void newStudent_savesAndReturnsStudent() {
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.newStudent(student);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals("alice@example.com", result.getEmail());
        verify(studentRepository).save(student);
    }

    // --- fetchbyid ---

    @Test
    void fetchbyid_whenStudentExists_returnsStudent() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        Student result = studentService.fetchbyid(1);

        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        assertEquals("Alice", result.getName());
    }

    @Test
    void fetchbyid_whenStudentDoesNotExist_returnsNull() {
        when(studentRepository.findById(999)).thenReturn(Optional.empty());

        Student result = studentService.fetchbyid(999);

        assertNull(result);
    }
}
