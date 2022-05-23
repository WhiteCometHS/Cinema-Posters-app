package com.milosh.lab04.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Set;

@Entity
@Table(name="typy")
@Getter
@Setter
@NoArgsConstructor
public class Type {
    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="nazwa")
    private String name;

    public Type(String name){
        this.name=name;
    }
}
