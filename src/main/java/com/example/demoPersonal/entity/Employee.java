package com.example.demoPersonal.entity;

import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "employee_projects",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects = new ArrayList<>();

    public Employee(String name, String email, String password, Position position) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.position = position;
    }

    public void addProject(Project project) {
        if(!this.projects.contains(project)) {
            this.projects.add(project);
            project.getEmployees().add(this);
        }
    }

    public void removeProject(Project project) {
        this.projects.remove(project);
        project.getEmployees().remove(this);
    }
}
