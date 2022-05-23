package com.milosh.lab04;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IllegalAuthorValidator implements ConstraintValidator<IllegalAuthor, String> {
    public IllegalAuthor constraint;
    @Override
    public void initialize(IllegalAuthor constraint) {
        this.constraint = constraint;
    }

    @Override
    public boolean isValid(String author, ConstraintValidatorContext constraintValidatorContext) {
        if (author != null && !author.isEmpty()) {
            if (constraint.ignoreCase() == false) {
                //sprawdzanie wartosci
                    if(Character.isLowerCase(author.charAt(0))){
                        return false;
                    }
            } else {
                    if(Character.isLowerCase(author.charAt(0))){
                        return false;
                    }
            }
        }
        return true;
    }
}
