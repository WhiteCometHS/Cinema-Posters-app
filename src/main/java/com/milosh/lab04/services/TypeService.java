package com.milosh.lab04.services;

import com.milosh.lab04.models.Time;
import com.milosh.lab04.models.Type;
import com.milosh.lab04.repository.TimeRepository;
import com.milosh.lab04.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    public List<Type> loadTypes(){
        return typeRepository.findAll();
    }
}
