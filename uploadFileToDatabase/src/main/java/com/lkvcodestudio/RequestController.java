package com.lkvcodestudio;


import com.lkvcodestudio.models.ImageGallary;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Optional;

@Controller
public class RequestController {
    @Autowired
    private ImageRepository imageRepository;


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("myfile") MultipartFile myfile) throws IOException {
        System.out.println("Image Size in Bytes - " + myfile.getBytes().length);
        ImageGallary img = new ImageGallary();
        img.setName(myfile.getOriginalFilename());
        img.setType(myfile.getContentType());
        img.setFileByte(myfile.getBytes());
        imageRepository.save(img);
        return "redirect:/";
    }

    @GetMapping("/images/{filename}")
    public void getImage(@PathVariable("filename") String filename, HttpServletResponse response) throws Exception {
        Optional<ImageGallary> img = imageRepository.findByName(filename);
        if(img.isPresent()) {
            System.out.println("returning file:"+filename);
            byte[] bytes = img.get().getFileByte();
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes));
            String mimeType = URLConnection.guessContentTypeFromStream(is);
            response.setContentType(mimeType);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }
    }

    @GetMapping("/")
    public String home(Model model){
        List<ImageGallary> images = imageRepository.findAll();
        images.forEach(img->{
            img.setBase64Img("data:image/png;base64,"+ Base64.encode(img.getFileByte()));
        });
        model.addAttribute("images",images);
        return "index";
    }


}
