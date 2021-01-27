package com.lkvcodestudio.pagination;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private Klass klass;
    private LocalDate dateOfAdmission;

    public Student(String name, String email, Klass klass, LocalDate dateOfAdmission) {
        this.name=name;
        this.email=email;
        this.klass=klass;
        this.dateOfAdmission = dateOfAdmission;
    }
}
