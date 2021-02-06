package com.lkvcodestudio;


import com.lkvcodestudio.models.ImageGallary;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class RequestController {
    @Autowired
    private ImageRepository imageRepository;

    @Value("${file.upload.location}")
    private String uploadLocation;

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("myfile") MultipartFile myfile) throws IOException {
        if (!myfile.isEmpty() && isRepositoryExists()) {
            String filename = getUniqueFileName(myfile.getOriginalFilename());
            OutputStream outputStream  = new FileOutputStream(uploadLocation+filename);
            outputStream.write(myfile.getBytes());
            ImageGallary img = new ImageGallary(filename, myfile.getContentType());
            imageRepository.save(img);
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ImageGallary> images = imageRepository.findAll();
        model.addAttribute("images", images);
        return "index";
    }

    public boolean isRepositoryExists() {
        File file = new File(uploadLocation);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
                return true;
            } else {
                System.out.println("Failed to create directory!");
                return false;
            }
        } else return true;
    }

    private String getUniqueFileName(String originalFilename) {
        String timestamp = new SimpleDateFormat("yyMMddHHmm").format(new Date());
        String filename = originalFilename.replace(" ","_");
        filename = timestamp+filename;
        return filename;
    }

}
