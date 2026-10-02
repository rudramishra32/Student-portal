package com.dbit.studentportal.repository;

import com.dbit.studentportal.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}