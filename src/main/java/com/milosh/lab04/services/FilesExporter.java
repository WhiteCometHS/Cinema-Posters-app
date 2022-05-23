package com.milosh.lab04.services;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.milosh.lab04.models.Bilet;
import com.milosh.lab04.models.Time;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Service
public class FilesExporter {
    public void setResponseHeader(HttpServletResponse response, String contentType, String extension, String prefix) throws IOException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = dateFormat.format(new Date());
        String fileName = prefix+timeStamp+extension;

        response.setContentType(contentType);

        String headerKey = "Header";
        String headerValue = "attachment; filename="+fileName;
        response.setHeader(headerKey,headerValue);
    }

    public void exportToPDF(Map<Bilet, Time> map, HttpServletResponse response) throws IOException{
        setResponseHeader(response,"application/pdf",".pdf","Bilet_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);
        Paragraph para = new Paragraph("Twój bilet",font);
        para.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(para);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        writeHeader(table);
        writeData(table, map);

        document.add(table);
        document.close();
    }
    private void writeHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.BLACK);
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Tytuł", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Data", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Cena", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Typ biletu", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Język/Czas trwania", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Godzina", font));
        table.addCell(cell);

    }
    private void writeData(PdfPTable table, Map<Bilet, Time> map){

        for(Bilet key : map.keySet()) {
            table.addCell(String.valueOf(key.getId()));
            table.addCell(key.getTitle());
            table.addCell(String.valueOf(key.getReleaseDate()));
            table.addCell(String.valueOf(key.getPrice()));
            table.addCell(String.valueOf(key.getType().getName()));
            table.addCell(key.getBiletFormat().getLanguage()+"/"+key.getBiletFormat().getRunningTime());
            Time value = map.get(key);
            table.addCell(String.valueOf(value.getTime()));
        }
    }
}
