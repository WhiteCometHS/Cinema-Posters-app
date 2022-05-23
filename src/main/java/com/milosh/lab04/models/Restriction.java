package com.milosh.lab04.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name="Restrictions")
@Getter
@Setter
@NoArgsConstructor
public class Restriction {
    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="restriction")
    private String restriction;

    public Restriction(String restriction){
        this.restriction=restriction;
    }
}
