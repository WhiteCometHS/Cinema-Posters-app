package com.milosh.lab04.repository;

import com.milosh.lab04.models.Time;
import com.milosh.lab04.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Integer> {
}
