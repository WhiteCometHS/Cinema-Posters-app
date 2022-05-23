package com.milosh.lab04.repository;
import com.milosh.lab04.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"bilets", "roles", "times"})
    @Query("SELECT p FROM User p WHERE p.username=:#{#username}")
    User findByUsername (String username);

    @EntityGraph(attributePaths = {"bilets", "roles", "times"})
    @Query("SELECT p FROM User p WHERE p.activationCode=:#{#code}")
    User findByActivationCode(String code);
}
