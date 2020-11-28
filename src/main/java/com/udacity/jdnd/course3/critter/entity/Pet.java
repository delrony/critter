package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.pet.PetType;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Pet {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type")
    private PetType type;

    @Nationalized
    private String name;

    private LocalDate birthDate;

    @Column(length = 512)
    private String notes;

    @ManyToOne
    private Customer customer;
}
