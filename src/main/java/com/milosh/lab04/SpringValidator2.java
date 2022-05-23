package com.milosh.lab04;

import com.milosh.lab04.models.Bilet;
import com.milosh.lab04.models.User;
import com.milosh.lab04.repository.UserRepository;
import com.milosh.lab04.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

public class SpringValidator2 implements Validator {

    @Override//Wspierana klasa
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override//Logika związana z poprawnością danych w obiekcie
    public void validate(Object o, Errors errors) {

        //ValidationUtils.rejectIfEmpty(errors, "author", "Empty.bilet.author");
        var something = (User) o;
        if (!something.isPasswordEquals()){
            errors.rejectValue("password", "Negative.user.password");
        }
    }
}
