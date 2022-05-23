package com.milosh.lab04.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, max = 36)
    private String username;
    @NotBlank
    private String password;
    @Transient//właściwość nie będzie odzwierciedlona w db
    private String passwordConfirm;
    private boolean enabled = false;//czy konto jest aktywne

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany
    @JoinTable(name = "users_bilets",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "bilet_id"))
    private Set<Bilet> bilets=new HashSet<>();

    @ManyToMany
    @JoinTable(name = "users_times",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "time_id"))
    private Set<Time> times=new HashSet<>();
    @NotBlank
    private String email;
    private String activationCode;


    public User(String username){
        this(username, false);
    }

    public User(String username, boolean enabled){
        this.username = username;
        this.enabled = enabled;
    }

    public boolean isPasswordEquals(){
        if(password==null|| passwordConfirm==null)
            return false;
        else
            return password.equals(passwordConfirm);
    }
}

