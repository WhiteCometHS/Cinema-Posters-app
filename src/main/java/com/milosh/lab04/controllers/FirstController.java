package com.milosh.lab04.controllers;

import com.milosh.lab04.SpringValidator;
import com.milosh.lab04.config.ProfileNames;
import com.milosh.lab04.controllers.filters.BiletSearchFilter;
import com.milosh.lab04.exceptions.BiletNotFoundException;
import com.milosh.lab04.formatter.MyFormatter;
import com.milosh.lab04.models.*;
import com.milosh.lab04.repository.BiletRepository;
import com.milosh.lab04.repository.GenreRepository;
import com.milosh.lab04.repository.ProducentRepository;
import com.milosh.lab04.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@Profile(ProfileNames.USE_NORMAL)
public class FirstController {
    @Autowired
    private BiletService biletService;
    @Autowired
    private ProducentService producentService;
    @Autowired
    private RestrictionService restrictionService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private TimeService timeService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserServiceImpl userService;


    @ModelAttribute("producenty")
    public List<Producent> loadLocations(){
        return producentService.loadLocations();
    }

    @ModelAttribute("restrictions")
    public List<Restriction> loadRestrictions(){
        return restrictionService.loadRestrictions();
    }

    @ModelAttribute("genres")
    public List<BiletGenre> loadBiletGenres(){
        return genreService.loadBiletGenres();
    }

    @ModelAttribute("times")
    public List<Time> loadBiletTimes(){
        return timeService.loadBiletTimes();
    }

    @ModelAttribute("types")
    public List<Type> loadTypes(){
        return typeService.loadTypes();
    }

    @ModelAttribute("biletFilter")
    public BiletSearchFilter loadEmptyFilter(){return new BiletSearchFilter();}

    @GetMapping("/start")
    public String start(){return "index";}

    @Secured("ROLE_ADMIN")
    @GetMapping("/form")
    public String showForm(@RequestParam(value="id", required=false)Long id,Model model) {
        if(id!=null){
            long temp=id;
            model.addAttribute("bilet",biletService.getBiletById(id));
            model.addAttribute("spec",id);
        }
        else{
            Bilet B = new Bilet();
            model.addAttribute("bilet",B);
            model.addAttribute("spec",null);
        }
        return "form";
    }
    @Secured("ROLE_ADMIN")
    @PostMapping("/form")
    public String processForm(@RequestParam(value="multipartFile", required=false) MultipartFile multipartFile, @RequestParam(value="id", required=false)Long id, @ModelAttribute("bilet") @Valid Bilet bilet, BindingResult result, Model model){
        if(result.hasErrors()){
            return "form";
        }
        biletService.addBilet(multipartFile,bilet,id);
        model.addAttribute("bilets",biletService.getBilets());
        return "redirect:/list";
    }
    @GetMapping("/bilet_image/{filename}")
    public void getImage(@PathVariable("filename") Long id, HttpServletResponse response) throws Exception {
        biletService.showImg(id,response);
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/delete")
    public String delete(@RequestParam(value="id")Long id,Model model) {
        biletService.deleteBilet(id);
        model.addAttribute("bilets", biletService.getBilets());
        return "redirect:/list";
    }
    @Transactional
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String zad3(Model model,@ModelAttribute("biletFilter") @Valid BiletSearchFilter filter,BindingResult result){
        if (filter.isEmpty()){
            model.addAttribute("bilets", biletService.getBilets());
        }
        else{
            if(result.hasErrors()){
                model.addAttribute("bilets", biletService.getBilets());
                return "list";
            }
            //model.addAttribute("bilets",biletRepository.findBiletsUsingNamedQuery(filter.getPhrase(),filter.getProducent(),filter.getMin(),filter.getMax(),filter.getDateMin(),filter.getDateMax(),filter.isBiletGenresEmpty()?null:filter.getBiletGenres()));
//            try(var biletStream=biletRepository.findBiletsUsingQuery(filter.getPhrase(),filter.getProducent(),filter.getMin(),filter.getMax(),filter.getDateMin(),filter.getDateMax(),filter.isBiletGenresEmpty()?null:filter.getBiletGenres())){
//                List<Bilet> temp = biletStream.collect(Collectors.toList());
//                model.addAttribute("bilets",temp);
//            }
            model.addAttribute("bilets",biletService.findBiletsUsingSpEL(filter));
        }
        return "list";
    }
    @Secured("ROLE_USER")
    @GetMapping("/additional")
    public String zad4(@RequestParam(value="id")Long id,Model model){
        model.addAttribute("bilet",biletService.getBiletById(id));
        model.addAttribute("genres",biletService.getBiletGenres(id));
        model.addAttribute("times",biletService.getBiletTimes(id));
        return "additional";
    }

    @PostMapping("/addReservation")
    public String addBiletReserv(@RequestParam(value="id")Long id,@RequestParam(value="time")Integer id_time, Authentication authentication){
        userService.addReservation(id,id_time,authentication.getName());

        return "redirect:/reservation";
    }

    @Secured("ROLE_USER")
    @GetMapping("/reservation")
    public String reservationList(Authentication authentication, Model model){
        model.addAttribute("bilets",userService.getBilets(authentication.getName()));
        return "reservation";
    }

    @Secured("ROLE_USER")
    @GetMapping("/exportPDF")
    public String exportPDF(@RequestParam(value="id")Long id,@RequestParam(value="id_time")Integer id_time,Authentication authentication, HttpServletResponse response) throws IOException{
        userService.exportToPDF(id,id_time,authentication.getName(),response);
        return "reservation";
    }

    @Secured("ROLE_USER")
    @GetMapping("/deleteRes")
    public String deltereservation(@RequestParam(value="id")Long id,@RequestParam(value="id_time")Integer id_time,Authentication authentication){
        userService.deleteReservation(id,id_time,authentication.getName());
        return "redirect:/reservation";
    }

    @InitBinder("bilet")
    public void initBinder(WebDataBinder binder){
        binder.addValidators(new SpringValidator());
        binder.setDisallowedFields("id");
        binder.addCustomFormatter(new MyFormatter());
    }
}
