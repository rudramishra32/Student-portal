package com.dbit.studentportal.service;

import com.dbit.studentportal.model.Department;
import com.dbit.studentportal.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Department not found with id: " + id
                ));
    }

    @Transactional
    public Department create(Department department) {
        return departmentRepository.save(department);
    }
}
