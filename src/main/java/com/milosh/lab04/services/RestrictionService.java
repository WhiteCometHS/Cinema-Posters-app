package com.milosh.lab04.services;

import com.milosh.lab04.models.Restriction;
import com.milosh.lab04.repository.RestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestrictionService {
    @Autowired
    private RestrictionRepository restrictionRepository;

    public List<Restriction> loadRestrictions(){
        return restrictionRepository.findAll();
    }
}
