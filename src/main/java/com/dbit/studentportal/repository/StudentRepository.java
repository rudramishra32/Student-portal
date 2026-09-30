package com.dbit.studentportal.repository;

import com.dbit.studentportal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find students whose name contains the given text (case-insensitive)
    List<Student> findByNameContainingIgnoreCase(String name);

    // Find students with CGPA greater than or equal to the given value
    List<Student> findByCgpaGreaterThanEqual(Double minCgpa);

    // Find a student by email
    Optional<Student> findByEmail(String email);

    // Check whether a student with the given email exists
    boolean existsByEmail(String email);

    // Check whether a student with the given roll number exists
    boolean existsByRollNo(String rollNo);


}