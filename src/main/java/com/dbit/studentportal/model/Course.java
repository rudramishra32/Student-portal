package com.dbit.studentportal.model;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
}
