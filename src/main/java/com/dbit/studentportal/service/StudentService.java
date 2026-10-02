package com.dbit.studentportal.service;

import com.dbit.studentportal.exception.StudentNotFoundException;
import com.dbit.studentportal.model.Department;
import com.dbit.studentportal.model.Student;
import com.dbit.studentportal.repository.DepartmentRepository;
import com.dbit.studentportal.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public StudentService(StudentRepository studentRepository,
                          DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with id: " + id
                        ));
    }

    @Transactional
    public Student create(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already registered: " + student.getEmail()
            );
        }

        attachDepartment(student);
        return studentRepository.save(student);
    }

    @Transactional
    public Student update(Long id, Student updated) {
        Student existing = findById(id);

        existing.setName(updated.getName());
        existing.setRollNo(updated.getRollNo());
        existing.setCgpa(updated.getCgpa());

        if (!existing.getEmail().equals(updated.getEmail())) {
            if (studentRepository.existsByEmail(updated.getEmail())) {
                throw new IllegalArgumentException(
                        "Email already in use: " + updated.getEmail()
                );
            }
            existing.setEmail(updated.getEmail());
        }

        if (updated.getDepartment() != null && updated.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(updated.getDepartment().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Department not found with id: " + updated.getDepartment().getId()
                    ));
            existing.setDepartment(dept);
        }

        return studentRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(
                    "Student not found with id: " + id
            );
        }

        studentRepository.deleteById(id);
    }

    public List<Student> searchByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> findTopStudents(Double minCgpa) {
        return studentRepository.findByCgpaGreaterThanEqual(minCgpa);
    }

    private void attachDepartment(Student student) {
        if (student.getDepartment() == null || student.getDepartment().getId() == null) {
            return;
        }

        Department dept = departmentRepository.findById(student.getDepartment().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Department not found with id: " + student.getDepartment().getId()
                ));
        student.setDepartment(dept);
    }
}
