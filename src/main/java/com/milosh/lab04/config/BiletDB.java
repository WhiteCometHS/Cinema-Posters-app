package com.milosh.lab04.config;

import com.milosh.lab04.models.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class BiletDB {
    public static ArrayList<Bilet> bilets;
    public static ArrayList<Producent> producentList;
    public static ArrayList<Type> typeList;
    public static ArrayList<Restriction> restrictionList;

    static{
        producentList = new ArrayList<>();
        producentList.add(new Producent("Touchstone Pictures","USA"));
        producentList.add(new Producent("Lionsgate Films","USA"));
        producentList.add(new Producent("Metro-Goldwyn-Mayer, Eon Productions","England"));

        typeList = new ArrayList<>();
        typeList.add(new Type("Normalny"));
        typeList.add(new Type("Studencki"));
        typeList.add(new Type("Dla dziecka"));

        restrictionList = new ArrayList<>();
        restrictionList.add(new Restriction("18+"));
        restrictionList.add(new Restriction("16+"));
        restrictionList.add(new Restriction("14+"));
        restrictionList.add(new Restriction("12+"));

        bilets = new ArrayList<>();
        Bilet A = new Bilet("Scott Waugh","Need For Speed", LocalDate.of(2014, Month.MARCH, 1),19.60f,false, producentList.get(0),typeList.get(0), restrictionList.get(1), 44);
        A.setBiletFormat(new BiletFormat("English","180 min"));
        bilets.add(A);
        A = new Bilet("Gary Ross","Hunger games",LocalDate.of(2012, Month.MARCH, 03),25.00f,true, producentList.get(1),typeList.get(0),restrictionList.get(1),36);
        A.setBiletFormat(new BiletFormat("Franch","180 min"));
        bilets.add(A);
        A = new Bilet("J.R.R. Tolkien","Lord of the Rings",LocalDate.of(2012, Month.FEBRUARY, 15),24.00f,true,producentList.get(2),typeList.get(0),restrictionList.get(2),50);
        A.setBiletFormat(new BiletFormat("Polish","180 min"));
        bilets.add(A);
        A = new Bilet("J.K. Rowling","Harry Potter and the Sorcerer's Stone",LocalDate.of(2015, Month.DECEMBER, 8),9.99f,true, producentList.get(0),typeList.get(0),restrictionList.get(3),57);
        A.setBiletFormat(new BiletFormat("English","180 min"));
        bilets.add(A);
    }

}