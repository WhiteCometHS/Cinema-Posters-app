package com.milosh.lab04.controllers;

import com.milosh.lab04.SpringValidator2;
import com.milosh.lab04.config.ProfileNames;
import com.milosh.lab04.models.User;
import com.milosh.lab04.services.UserService;
import com.milosh.lab04.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Profile(ProfileNames.USE_NORMAL)
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("user", new User());
        return "registrationForm";
    }
    @PostMapping("/registration")
    public String registration(@ModelAttribute("user")@Valid User userForm, BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()||!userForm.isPasswordEquals()){
            return "registrationForm";
        }
        if(userService.checkUsernames(userForm)){
            bindingResult.rejectValue("username", "Negative.user.username");
            return "registrationForm";
        }
        model.addAttribute("reg", true);
        userService.saveUser(userForm);

        return "index";
    }
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        User user = userService.findByActivationCode(code);
        if(user == null)
        {
            model.addAttribute("spec", false);
        }
        else
        {
            userService.removeCode(user);
            model.addAttribute("spec", true);
        }
        return "login";
    }

    @InitBinder("user")
    public void initBinder(WebDataBinder binder){
        binder.addValidators(new SpringValidator2());
        binder.setDisallowedFields("enabled");
    }
}
