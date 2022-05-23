package com.milosh.lab04.repository;

import com.milosh.lab04.models.Bilet;
import com.milosh.lab04.models.Time;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TimeRepository extends JpaRepository<Time, Integer> {
    @EntityGraph(attributePaths = {"users"})
    @Query("SELECT p FROM Time p WHERE p.id=:#{#id}")
    Time getById(Integer id);
}
