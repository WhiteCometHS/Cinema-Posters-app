package com.milosh.lab04.repository;

import com.milosh.lab04.controllers.filters.BiletSearchFilter;
import com.milosh.lab04.models.Bilet;
import com.milosh.lab04.models.BiletGenre;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BiletRepository extends JpaRepository<Bilet, Long> {
    List<Bilet> findBiletsByTitleIgnoreCaseContainingOrAuthorIgnoreCaseContaining(String phrase1, String phrase2);

    List<Bilet> findBiletsUsingNamedQuery(String phrase, String producent, Float Min, Float Max, Date dateMin, Date dateMax,List<BiletGenre> biletGenres);

    @Query("SELECT p FROM Bilet p WHERE "+
            "("+
            ":phrase is null or :phrase ='' "+
            "or upper(p.title) like upper(:phrase) "+
            "or upper(p.author) like upper(:phrase) "+
            ") "+
            "and "+
            "(:Min is null or :Min <= p.price) "+
            "and (:Max is null or :Max >= p.price) "+
            "and (:dateMin is null or :dateMin <= p.releaseDate) "+
            "and (:dateMax is null or :dateMax >= p.releaseDate) "+
            "and (:producent is null or p.producent.name in :producent) "+
            "and (coalesce(:biletGenres) is null or exists (select g from p.genres g where g in :biletGenres))"+
            "")
    Stream<Bilet> findBiletsUsingQuery(String phrase,String producent,Float Min,Float Max,Date dateMin,Date dateMax,List<BiletGenre> biletGenres);


    @EntityGraph(attributePaths = {"producent", "genres", "times", "users", "type"})
    @Query("SELECT p FROM Bilet p WHERE "+
            "("+
            ":#{#filter.phrase} is null or :#{#filter.phrase} ='' "+
            "or upper(p.title) like upper(:#{#filter.phrase}) "+
            "or upper(p.author) like upper(:#{#filter.phrase}) "+
            ") "+
            "and "+
            "(:#{#filter.Min} is null or :#{#filter.Min} <= p.price) "+
            "and (:#{#filter.Max} is null or :#{#filter.Max} >= p.price) "+
            "and (:#{#filter.dateMin} is null or :#{#filter.dateMin} <= p.releaseDate) "+
            "and (:#{#filter.dateMax} is null or :#{#filter.dateMax} >= p.releaseDate) "+
            "and (:#{#filter.producent} is null or p.producent.name in :#{#filter.producent}) "+
            "and (coalesce(:#{#filter.biletGenres}) is null or exists (select g from p.genres g where g in :#{#filter.biletGenres}))"+
            "")
    List<Bilet> findBiletsUsingSpEL(@Param("filter") BiletSearchFilter filter);


    @EntityGraph(attributePaths = {"producent", "genres", "times", "users", "type"})
    @Query("SELECT p FROM Bilet p")
    List<Bilet> findAll();

    @EntityGraph(attributePaths = {"producent","restriction", "genres", "times", "users", "type"})
    @Query("SELECT p FROM Bilet p WHERE p.id=:#{#id}")
    Optional<Bilet> findById(Long id);

    @EntityGraph(attributePaths = {"producent","restriction", "genres","times", "users", "type"})
    @Query("SELECT p FROM Bilet p WHERE exists (select g from p.users g where g.username in :#{#username})")
    List<Bilet> findByUsername(String username);
}
