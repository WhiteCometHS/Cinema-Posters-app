package com.milosh.lab04.services;

import com.milosh.lab04.models.Producent;
import com.milosh.lab04.repository.ProducentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
@Service
public class ProducentService {
    @Autowired
    private ProducentRepository producentRepository;

    public List<Producent> loadLocations(){
        return producentRepository.findAll();
    }
}
