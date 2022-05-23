package com.milosh.lab04;

import com.milosh.lab04.models.Bilet;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class SpringValidator implements Validator {


    @Override//Wspierana klasa
    public boolean supports(Class<?> aClass) {
        return Bilet.class.isAssignableFrom(aClass);
    }

    @Override//Logika związana z poprawnością danych w obiekcie
    public void validate(Object o, Errors errors) {

        //ValidationUtils.rejectIfEmpty(errors, "author", "Empty.bilet.author");
        var vehicle = (Bilet) o;
        if (vehicle.getAuthor().equals(vehicle.getTitle())) {
            errors.rejectValue("author", "Negative.bilet.author");
        }
    }
}
