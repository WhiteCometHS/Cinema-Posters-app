package com.milosh.lab04.services;

import com.milosh.lab04.config.ProfileNames;
import com.milosh.lab04.models.Bilet;
import com.milosh.lab04.models.Role;
import com.milosh.lab04.models.Time;
import com.milosh.lab04.models.Type;
import com.milosh.lab04.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Service("userDetailsService")
@Profile(ProfileNames.USERS_IN_DATABASE)
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BiletRepository biletRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private FilesExporter export;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return convertToUserDetails(user);
    }

    private UserDetails convertToUserDetails(com.milosh.lab04.models.User user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getType().toString()));
        }

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);//UWAGA: klasa ma też drugi konstruktor – więcej parametrów!!!
    }
    @Override
    public boolean checkUsernames(com.milosh.lab04.models.User user){
        List<com.milosh.lab04.models.User>users=userRepository.findAll();
        for(int i=0;i<users.size();i++){
            if(user.getUsername().equals(users.get(i).getUsername())){
                return true;
            }
        }
        return false;
    }
    @PreAuthorize("isAnonymous() or isAuthenticated()")
    @Override
    public void saveUser(com.milosh.lab04.models.User user){

        var userRole =roleRepository.findByType(Role.Types.ROLE_USER);
        var roles =new HashSet<Role>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordConfirm(user.getPassword());
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        if(!ObjectUtils.isEmpty(user.getEmail()))
        {
            String message = String.format("Hello, %s! \n"+"Welcome to my project. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public void addReservation(Long id, Integer id_time, String username){
        com.milosh.lab04.models.User user = userRepository.findByUsername(username);
        Time time = timeRepository.findById(id_time).get();
        Bilet bilet= biletRepository.findById(id).get();
        var bilets=user.getBilets();
        var times=user.getTimes();
        bilets.add(bilet);
        user.setBilets(bilets);
        times.add(time);
        user.setTimes(times);
        userRepository.save(user);
        bilet.setAmount(bilet.getAmount()-1);
        bilet.setEditable(false);
        biletRepository.save(bilet);
    }

    public  Map<Bilet, Time> getBilets(String username){
        List<Bilet> bilets = biletRepository.findByUsername(username);
        Set<Time> times = userRepository.findByUsername(username).getTimes();
        Map<Bilet, Time> map = new HashMap<>();
        int i=0;
        for (Iterator<Time> it = times.iterator(); it.hasNext(); ) {
            Time t = it.next();
            map.put(bilets.get(i),t);
            i++;
        }
        return map;
    }
    public void deleteReservation(Long id, Integer id_time, String username){
        com.milosh.lab04.models.User user = userRepository.findByUsername(username);
        Time time = timeRepository.findById(id_time).get();
        Bilet bilet= biletRepository.findById(id).get();
        var bilets=user.getBilets();
        var times=user.getTimes();
        for (Iterator<Bilet> iterator = bilets.iterator(); iterator.hasNext();) {
            Bilet b =  iterator.next();
            if (b.getId() == id) {
                iterator.remove();
            }
        }
        for (Iterator<Time> iterator = times.iterator(); iterator.hasNext();) {
            Time t =  iterator.next();
            if (t.getId() == id_time) {
                iterator.remove();
            }
        }
        user.setBilets(bilets);
        user.setTimes(times);
        userRepository.save(user);
        boolean temp=false;
        for (Iterator<Bilet> iterator = bilets.iterator(); iterator.hasNext();) {
            Bilet b =  iterator.next();
            if (b.getId() == id) {
                temp=true;
                break;
            }
        }
        if(!temp){
            bilet.setEditable(true);
            biletRepository.save(bilet);
        }
    }
    public void removeCode(com.milosh.lab04.models.User user){
        user.setEnabled(true);
        user.setActivationCode(null);
        userRepository.save(user);
    }
    public com.milosh.lab04.models.User findByActivationCode(String code){
        return userRepository.findByActivationCode(code);
    }
    public void exportToPDF(Long id, Integer id_time, String username, HttpServletResponse response)throws IOException {
        com.milosh.lab04.models.User user = userRepository.findByUsername(username);
        Time time = timeRepository.findById(id_time).get();
        Bilet bilet= biletRepository.findById(id).get();
        Map<Bilet, Time> map = new HashMap<>();
        map.put(bilet,time);
        export.exportToPDF(map,response);
    }

}
