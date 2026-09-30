package com.dbit.studentportal.model;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;


@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @Email
    private String email;

    private String rollNo;
    private Double cgpa;



    // Constructors
    public Student() {
    }

    public Student(String name, String email, String rollNo, Double cgpa) {
        this.name = name;
        this.email = email;
        this.rollNo = rollNo;
        this.cgpa = cgpa;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String r) {
        this.rollNo = r;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double c) {
        this.cgpa = c;
    }

    @Transient
    private String displayLabel;

    public String getDisplayLabel() {
        return rollNo + " - " + name;
    }
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

}
