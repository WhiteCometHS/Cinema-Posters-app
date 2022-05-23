package com.milosh.lab04.controllers.filters;

import com.milosh.lab04.models.BiletGenre;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BiletSearchFilter {
    private String phrase;
    private String producent;
    private Float Min;
    private Float Max;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateMin;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateMax;
    private List<BiletGenre> biletGenres;

    public BiletSearchFilter(){
        biletGenres=new ArrayList<>();
    }
    public boolean isBiletGenresEmpty() {
        return biletGenres.isEmpty();
    }

    public Date getDateMin(){
        try{
            Date temp = Date.valueOf(dateMin);
            return temp;
        }catch (NullPointerException ex){
            return null;
        }
    }
    public Date getDateMax(){
        try{
            Date temp = Date.valueOf(dateMax);
            return temp;
        }catch (NullPointerException ex){
            return null;
        }
    }
    public String getPhrase() {
        return phrase;
    }
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
    public boolean isEmpty() {
        if((phrase==null|| phrase=="") && producent==null)
            return true;
        else
            return false;
        //return StringUtils.isEmptyOrWhitespace(phrase);
    }
    public String getProducent(){
        try{
            int temp=Integer.parseInt(producent);
            return null;
        }
        catch (NumberFormatException e) {
            return producent;
        }
    }
    public void clear(){phrase="";}

}
