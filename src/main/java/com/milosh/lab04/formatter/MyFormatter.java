package com.milosh.lab04.formatter;

import com.milosh.lab04.models.BiletFormat;
import org.springframework.format.Formatter;
import java.text.ParseException;
import java.util.Locale;

public class MyFormatter implements Formatter<BiletFormat> {
    @Override
    public BiletFormat parse(String text, Locale locale) throws ParseException {

        var tokens =text.split(";");
        if(tokens.length==2){
                String a=tokens[0];
                String b=tokens[1];
                return new BiletFormat(a,b);
        }
        throw new ParseException("Nieprawidłowy format danych",0);
    }

    @Override
    public String print(BiletFormat format, Locale locale){
        if(format==null || format.getLanguage()==null && format.getRunningTime()==null){
            return "";
        }
        return String.format("%s;%s", format.getLanguage(), format.getRunningTime());
    }
}
