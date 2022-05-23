package com.milosh.lab04.services;

import com.milosh.lab04.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void saveUser(User userForm);
    boolean checkUsernames(User user);
    User findByActivationCode(String code);
}
