package com.example.demo.controllers;

import com.example.demo.repositories.S3FileRepository;
import com.example.demo.services.S3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

@Controller
public class S3BucketController {

    @Autowired
    S3BucketService s3BucketService;
    @Autowired
    S3FileRepository s3FileRepository;

    @GetMapping
    public String homePage(Model model){
        model.addAttribute("myfiles",s3FileRepository.findAll(Sort.by(Sort.Direction.DESC,"id")));
        return "index";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        this.s3BucketService.uploadFile(file);
        return "redirect:/";
    }

    @GetMapping("/deleteFile")
    @ResponseBody
    public String deleteFile(@RequestParam(value = "filename") String filename) {
        return this.s3BucketService.deleteFileFromS3Bucket(filename);
    }

    @GetMapping(value = "/storage/**")
    public void thumbnail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filename = request.getRequestURI();
        filename = filename.substring(9);
        byte[] bytes  = this.s3BucketService.getFile(filename);
        InputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes));
        String mimeType = URLConnection.guessContentTypeFromStream(is);
        response.setContentType(mimeType);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

}

