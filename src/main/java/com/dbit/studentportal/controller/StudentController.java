package com.dbit.studentportal.controller;

import com.dbit.studentportal.model.Student;
import com.dbit.studentportal.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    private final StudentService studentService;

    // Constructor injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ─────────────────── GET ───────────────────

    // GET /api/v1/students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.findAll();
        return ResponseEntity.ok(students);
    }

    // GET /api/v1/students/5
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable Long id) {

        Student student = studentService.findById(id);
        return ResponseEntity.ok(student);
    }

    // GET /api/v1/students/search?name=priya
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(
            @RequestParam String name) {

        List<Student> results = studentService.searchByName(name);
        return ResponseEntity.ok(results);
    }

    // GET /api/v1/students/top?minCgpa=8.5
    @GetMapping("/top")
    public ResponseEntity<List<Student>> getTopStudents(
            @RequestParam(defaultValue = "8.0") Double minCgpa) {

        return ResponseEntity.ok(
                studentService.findTopStudents(minCgpa)
        );
    }

    // ─────────────────── POST ───────────────────

    // POST /api/v1/students
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Valid @RequestBody Student student) {

        Student created = studentService.create(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    // ─────────────────── PUT ───────────────────

    // PUT /api/v1/students/5
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {

        Student updated = studentService.update(id, student);

        return ResponseEntity.ok(updated);
    }

    // ─────────────────── PATCH ───────────────────

    // PATCH /api/v1/students/5/cgpa
    @PatchMapping("/{id}/cgpa")
    public ResponseEntity<Student> updateCgpa(
            @PathVariable Long id,
            @RequestParam Double cgpa) {

        Student student = studentService.findById(id);
        student.setCgpa(cgpa);

        return ResponseEntity.ok(
                studentService.update(id, student)
        );
    }

    // ─────────────────── DELETE ───────────────────

    // DELETE /api/v1/students/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {

        studentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}