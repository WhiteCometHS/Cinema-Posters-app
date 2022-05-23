package com.milosh.lab04.controllers;

import com.milosh.lab04.exceptions.BiletNotFoundException;

import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Log4j2
public class GlobalControllerAdvice {

    @ExceptionHandler(BiletNotFoundException.class)
    public String handleBiletNotFoundError(Model model, HttpServletRequest req, Exception ex){
        log.error("Request: "+ req.getRequestURL()+" raised "+ex);

        model.addAttribute("exception", ex);
        model.addAttribute("url", req.getRequestURL());

        return "error404biletNotFound";
    }

    @ExceptionHandler({JDBCConnectionException.class, DataIntegrityViolationException.class})
    public String handleDbError(Model model, HttpServletRequest req, Exception ex){
        log.error("Request: "+ req.getRequestURL()+" raised "+ex);

        model.addAttribute("exception", ex);
        model.addAttribute("url", req.getRequestURL());
        return "error500";
    }

}
