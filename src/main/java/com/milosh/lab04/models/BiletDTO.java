package com.milosh.lab04.models;

import com.milosh.lab04.services.BiletService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class BiletDTO {
    @Autowired
    private BiletService biletService;

    private Integer id;
    private String author;
    private String title;
    private LocalDate releaseDate;
    private float price;
    private boolean bestseller;
    private Set<Integer> genresId;
    private Integer producentId;

    public void setProducentId(Producent m){
        this.producentId=m.getId();
    }
    public void setGenresId(Set<BiletGenre> bg){
        genresId=bg.stream().map(x->x.getId()).collect(Collectors.toSet());
    }
}
