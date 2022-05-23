package com.milosh.lab04.services;

import com.milosh.lab04.models.BiletGenre;
import com.milosh.lab04.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
@Service
public class GenreService {
    @Autowired
    private GenreRepository genreRepository;

    public List<BiletGenre> loadBiletGenres(){
        return genreRepository.findAll();
    }
}
