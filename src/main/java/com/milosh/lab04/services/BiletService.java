package com.milosh.lab04.services;

import com.milosh.lab04.controllers.filters.BiletSearchFilter;
import com.milosh.lab04.exceptions.BiletNotFoundException;
import com.milosh.lab04.models.Bilet;
import com.milosh.lab04.models.BiletGenre;
import com.milosh.lab04.models.Time;
import com.milosh.lab04.repository.BiletRepository;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@PropertySource("config.properties")
public class BiletService {
    @Autowired
    private BiletRepository biletRepository;
    @Value("${files.location}")
    private String uploadPath;
    @Value("${image.size_min}")
    private Integer imageSize;
    @Value("${image.size_medium}")
    private Integer imageSize2;
    @Value("${image.size_large}")
    private Integer imageSize3;

    private Logger logger = LoggerFactory.getLogger(BiletService.class);

    public List<Bilet> getBilets() {
        return biletRepository.findAll();
    }
    public Bilet getBiletById(Long id) {
        return biletRepository.findById(id).orElseThrow(() -> new BiletNotFoundException(id));
    }
    public List<Bilet> findBiletsUsingSpEL(BiletSearchFilter filter) {
        return biletRepository.findBiletsUsingSpEL(filter);
    }
    public Set<BiletGenre> getBiletGenres(Long id) {
        return biletRepository.findById(id).orElseThrow().getGenres();
    }
    public Set<Time> getBiletTimes(Long id) {
        return biletRepository.findById(id).orElseThrow().getTimes();
    }
    public void saveBilet(Bilet bilet){ biletRepository.save(bilet); }
    public void addBilet(MultipartFile multipartFile, Bilet bilet, Long id){

        if(multipartFile != null && multipartFile.isEmpty() == false) {
            try {
                addImage(bilet,multipartFile);
            }catch(IOException e){}

        }
        if(id==null){
            saveBilet(bilet);
        }
        else{
            deleteBilet(id);
            saveBilet(bilet);
        }
    }
    public void deleteBilet(Long id){
        biletRepository.deleteById(id);
    }
    public void addImage(Bilet bilet,MultipartFile file)throws IOException{
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists())
        {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + FilenameUtils.removeExtension(file.getOriginalFilename())+".png";

        File temp = new File(uploadPath + "/" + resultFilename);
        bilet.setFileName(resultFilename);
        //
        file.transferTo(temp);

        //resizeImage(temp, imageSize2);
        //resizeImage(temp, imageSize3);
    }
    public void showImg(long id, HttpServletResponse response)throws Exception{
        Bilet img = getBiletById(id);
        if(img!=null) {
            byte[] bytes = img.getFileContent();
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes));
            String mimeType = URLConnection.guessContentTypeFromStream(is);
            response.setContentType(mimeType);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }
    }
    public boolean resizeImage(File sourceFile,Integer size) {
        try {
            BufferedImage bufferedImage = ImageIO.read(sourceFile);
            BufferedImage outputImage = Scalr.resize(bufferedImage, size);
            String newFileName = FilenameUtils.getBaseName(sourceFile.getName())
                    + "_" + size.toString() + ".png";
            System.out.println(FilenameUtils.getBaseName(sourceFile.getName()));
            Path path = Paths.get(uploadPath,newFileName);
            File newImageFile = path.toFile();

            ImageIO.write(outputImage, "png", newImageFile);
            outputImage.flush();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
