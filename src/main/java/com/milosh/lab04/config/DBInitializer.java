package com.milosh.lab04.config;

import com.milosh.lab04.models.*;
import com.milosh.lab04.repository.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalTime;
import java.util.*;

@Configuration
public class DBInitializer {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BiletRepository biletRepository;
    @Autowired
    private ProducentRepository producentRepository;
    @Autowired
    private RestrictionRepository restrictionRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Bean
    InitializingBean init() {
        return () -> {
            if(roleRepository.findAll().isEmpty()){
                Role roleUser = roleRepository.save(new Role(Role.Types.ROLE_USER));
                Role roleAdmin = roleRepository.save(new Role(Role.Types.ROLE_ADMIN));

                User user = new User("user", true);
                user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                user.setPassword(passwordEncoder.encode("user"));
                user.setEmail("test@gmail.com");

                User admin = new User("admin", true);
                admin.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("test@gmail.com");

                User test = new User("superuser", true);
                test.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleUser)));
                test.setPassword(passwordEncoder.encode("superuser"));
                test.setEmail("test@gmail.com");

                userRepository.save(user);
                userRepository.save(admin);
                userRepository.save(test);
            }
            if (producentRepository.findAll().isEmpty()) {
                for (var l : BiletDB.producentList) {
                    l.setId(null);
                    producentRepository.save(l);
                }
            }
            if (restrictionRepository.findAll().isEmpty()) {
                for (var l : BiletDB.restrictionList) {
                    l.setId(null);
                    restrictionRepository.save(l);
                }
            }
            if(typeRepository.findAll().isEmpty()){
                for (var l : BiletDB.typeList) {
                    typeRepository.save(l);
                }
            }
            if(timeRepository.findAll().isEmpty()){
                //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                timeRepository.save(new Time(LocalTime.of(9,30)));
                timeRepository.save(new Time(LocalTime.of(12,00)));
                timeRepository.save(new Time(LocalTime.of(14,30)));
                timeRepository.save(new Time(LocalTime.of(16,00)));
                timeRepository.save(new Time(LocalTime.of(18,30)));
                timeRepository.save(new Time(LocalTime.of(20,30)));
            }
            if (biletRepository.findAll().isEmpty()) {
                var generator=new Random();
                for (var pic : BiletDB.bilets) {
                    int temp = generator.nextInt(timeRepository.findAll().size()-1+1)+1;
                    pic.getTimes().add(timeRepository.getById(temp));

                    biletRepository.save(pic);
                }
            }

            if (genreRepository.findAll().isEmpty()) {
                genreRepository.save(new BiletGenre("Horror"));
                genreRepository.save(new BiletGenre("Action thriller"));
                genreRepository.save(new BiletGenre("Fantasy"));
                genreRepository.save(new BiletGenre("Adventure"));
                genreRepository.save(new BiletGenre("Drama"));

                var gatunki=genreRepository.findAll();
                var generator=new Random();
                for (var i = 0; i < 100; i++) {
                    for (var pic : biletRepository.findAll()) {
                        pic.setGenres(new HashSet<>());
                        for(int y=0,n=generator.nextInt(2)+1;y<n;y++){
                            var idx=generator.nextInt(5);
                            pic.getGenres().add(gatunki.get(idx));
                        }
                        biletRepository.save(pic);
                    }
                }
            }
        };
    }
}
