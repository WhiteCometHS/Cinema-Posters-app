package com.milosh.lab04.models;

import com.milosh.lab04.IllegalAuthor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="bilety")
@NamedQuery(
        name="Bilet.findBiletsUsingNamedQuery",
        query="SELECT p FROM Bilet p WHERE "+
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
                "and (coalesce(:biletGenres) is null or exists (select g from p.genres g where g in :biletGenres))"
)
public class Bilet {
    private static long idSpec;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min=5, max=20, message="Size must be between {min} and {max} marks")
    @IllegalAuthor
    private String author;
    @Size(min=5, max=40, message="Tytul musi mieć rozmiar od {min} do {max} znaków")
    private String title;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate releaseDate;
    @NumberFormat(pattern = "#.00")
    @Min(0) @Max(300)
    @NotNull
    private float price;
    private boolean bestseller;
    private String fileName;
    @Lob
    private byte[] fileContent;
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    private Producent producent;
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    private Restriction restriction;
    @NotNull
    @Embedded
    private BiletFormat biletFormat;
    @ManyToMany(fetch = FetchType.LAZY)
    @NotEmpty
    private Set<BiletGenre> genres=new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @NotEmpty
    private Set<Time> times=new HashSet<>();
    @ManyToMany(mappedBy = "bilets")
    private Set<User> users;
    @Min(1) @Max(300)
    private int amount;
    private boolean editable=true;
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;


    public Bilet() {
        this.producent=new Producent();
        this.type=new Type();
        this.restriction=new Restriction();
        this.biletFormat=new BiletFormat();
    }

    public Bilet(String author, String title, LocalDate releaseDate, float price, boolean bestseller, Producent producent,Type type, Restriction restriction, int amount){
        this.author=author;
        this.title=title;
        this.price=price;
        this.releaseDate=releaseDate;
        this.bestseller=bestseller;
        this.producent=producent;
        this.type=type;
        this.restriction=restriction;
        this.amount=amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public void setRestriction(Restriction restriction) {
        this.restriction = restriction;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Time> getTimes() {
        return times;
    }

    public void setTimes(Set<Time> times) {
        this.times = times;
    }

    public void setGenresId(Set<Integer> genresIds){
        genres = genresIds.stream().map(x-> new BiletGenre(x)).collect(Collectors.toSet());
    }
    public void setProducentId(Integer Id){
        producent= new Producent(Id);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public Set<BiletGenre> getGenres() {
        return genres;
    }

    public void setGenres(Set<BiletGenre> genres) {
        this.genres = genres;
    }

    public void incrementId(){
        idSpec++;
    }

    public static Long getIdSpec() {
        return idSpec;
    }

    public BiletFormat getBiletFormat() {
        return biletFormat;
    }

    public void setBiletFormat(BiletFormat biletFormat) {
        this.biletFormat = biletFormat;
    }

    public Producent getProducent() {
        return producent;
    }

    public void setProducent(Producent producent) {
        this.producent = producent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isBestseller() {
        return bestseller;
    }

    public void setBestseller(boolean bestseller) {
        this.bestseller = bestseller;
    }
}
