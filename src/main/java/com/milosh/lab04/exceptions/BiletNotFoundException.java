package com.milosh.lab04.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such bilet")
public class BiletNotFoundException extends RuntimeException {

    public BiletNotFoundException(long id){
        super("Bilet o id "+id+" nie istnieje");
    }

}
