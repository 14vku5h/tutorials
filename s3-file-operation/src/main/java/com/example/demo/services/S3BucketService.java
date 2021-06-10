package com.example.demo.services;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.demo.models.S3File;
import com.example.demo.repositories.S3FileRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
@Log
@Service
public class S3BucketService {
    @Autowired
    S3FileRepository s3FileRepository;
    @Value("${s3.bucket.name}")
    String bucketName;
    @Value("${s3.bucket.region}")
    String region;
    String foldername = "images";

    AmazonS3 s3Client;

    @PostConstruct
    private void initialiseS3Bucket() {
        try {
            s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .build();
            log.info("s3Client initialized ");
        }catch (AmazonS3Exception ex){
            ex.printStackTrace();
        }
    }


    public boolean uploadFile(MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = foldername+ "/" + generateFileName(multipartFile);
            if(uploadFileTos3bucket(fileName, file)){
                S3File s3file = new S3File(fileName,multipartFile.getContentType());
                s3FileRepository.save(s3file);
            }
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return multiPart.getOriginalFilename().replace(" ", "_")+new Date().getTime() + "-" ;
    }

    private boolean uploadFileTos3bucket(String fileName, File file) {
        try {
            PutObjectResult result = s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public String deleteFileFromS3Bucket(String fileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            S3File s3File = s3FileRepository.findByFileName(fileName);
            s3FileRepository.delete(s3File);
            return "Successfully deleted";
        }catch (Exception e){
          return e.getMessage();
        }
    }


    public byte[] getFile(String filename) {
        byte[] bytearray = null;
        try {
            S3Object img = s3Client.getObject(new GetObjectRequest(bucketName,filename));
            bytearray= IOUtils.toByteArray(img.getObjectContent());

        }catch (Exception e){
            System.out.println("File fetch Error");
            e.printStackTrace();}
        return bytearray;
    }

    public String getEndpointUrl(){
        String endpointUrl= "https://"+bucketName+".s3."+region+".amazonaws.com";
        return endpointUrl;
    }

}
