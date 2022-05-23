package com.milosh.lab04.services;

import com.milosh.lab04.models.BiletGenre;
import com.milosh.lab04.models.Time;
import com.milosh.lab04.repository.GenreRepository;
import com.milosh.lab04.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TimeService {
    @Autowired
    private TimeRepository timeRepository;

    public List<Time> loadBiletTimes(){
        return timeRepository.findAll();
    }
}
