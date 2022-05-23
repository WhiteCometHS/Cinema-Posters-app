package com.milosh.lab04.repository;

import com.milosh.lab04.models.Producent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducentRepository extends JpaRepository<Producent, Integer> {
}
