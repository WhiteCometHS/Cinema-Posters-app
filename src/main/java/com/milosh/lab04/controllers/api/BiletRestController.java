package com.milosh.lab04.controllers.api;

import com.milosh.lab04.SpringValidator;
import com.milosh.lab04.config.ProfileNames;
import com.milosh.lab04.controllers.filters.BiletSearchFilter;
import com.milosh.lab04.formatter.MyFormatter;
import com.milosh.lab04.models.Bilet;
import com.milosh.lab04.models.BiletDTO;
import com.milosh.lab04.models.BiletGenre;
import com.milosh.lab04.models.Producent;
import com.milosh.lab04.services.BiletService;
import com.milosh.lab04.services.GenreService;
import com.milosh.lab04.services.ProducentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Profile(ProfileNames.USE_REST)
public class BiletRestController {
    @Autowired
    private BiletService biletService;
    @Autowired
    private ProducentService producentService;
    @Autowired
    private GenreService genreService;
    private final ModelMapper modelMapper = new ModelMapper();

    @ModelAttribute("producenty")
    public List<Producent> loadLocations(){
        return producentService.loadLocations();
    }

    @ModelAttribute("genres")
    public List<BiletGenre> loadBiletGenres(){
        return genreService.loadBiletGenres();
    }

    @ModelAttribute("biletFilter")
    public BiletSearchFilter loadEmptyFilter(){return new BiletSearchFilter();}

    @GetMapping("/additional")
    public Bilet zad4(@RequestParam(value="id")Long id){

        return biletService.getBiletById(id);
    }
    @GetMapping("/delete")
    public List<Bilet> delete(@RequestParam(value="id")Long id) {
        biletService.deleteBilet(id);
        return biletService.getBilets();
    }
    @GetMapping("/deleteDTO")
    public List<BiletDTO> deleteDTO(@RequestParam(value="id")Long id) {
        biletService.deleteBilet(id);
        List<BiletDTO> list=new ArrayList<>();
        for (Bilet a:biletService.getBilets()) {
            var dto=modelMapper.map(a,BiletDTO.class);
            dto.setGenresId(a.getGenres());
            dto.setProducentId(a.getProducent());
            list.add(dto);
        }
        return list;
    }
//    @PostMapping("/add")
//    public void add(@RequestBody Bilet bilet) {
//        biletService.saveBilet(bilet);
//    }
    @RequestMapping(value = "/add",
            produces = "application/json",
            method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)//201 Created
    public void createBook(@RequestBody Bilet bilet) {
        biletService.saveBilet(bilet);
    }

    @RequestMapping(value = "/addDTO",
            produces = "application/json",
            method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)//201 Created
    public void createBookDTO(@RequestBody BiletDTO dto) {
        var bilet=modelMapper.map(dto,Bilet.class);
        bilet.setGenresId(dto.getGenresId());
        bilet.setProducentId(dto.getProducentId());
        biletService.saveBilet(bilet);
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public List<Bilet> zad3(){
        return biletService.getBilets();
    }

    @RequestMapping(value = "/listDTO", method = {RequestMethod.GET, RequestMethod.POST})
    public List<BiletDTO> listDTO(){
        List<BiletDTO> list=new ArrayList<>();
        for (Bilet a:biletService.getBilets()) {
            var dto=modelMapper.map(a,BiletDTO.class);
            dto.setGenresId(a.getGenres());
            dto.setProducentId(a.getProducent());
            list.add(dto);
        }
        return list;
    }

    @GetMapping("/additionalDTO")
    public BiletDTO biletDetails(@RequestParam(value="id")Long id){
        var bilet = biletService.getBiletById(id);
        var dto=modelMapper.map(bilet,BiletDTO.class);
        dto.setGenresId(bilet.getGenres());
        dto.setProducentId(bilet.getProducent());
        return dto;
    }

    @InitBinder("bilet")
    public void initBinder(WebDataBinder binder){
        binder.addValidators(new SpringValidator());
        binder.setDisallowedFields("id","releaseDate");
        binder.addCustomFormatter(new MyFormatter());
    }
}
