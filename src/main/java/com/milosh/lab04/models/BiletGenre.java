package com.milosh.lab04.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="genres")
@Data
@NoArgsConstructor
public class BiletGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public BiletGenre(String name){
        this.name = name;
    }
    public BiletGenre(Integer id){
        this.id = id;
    }
}
