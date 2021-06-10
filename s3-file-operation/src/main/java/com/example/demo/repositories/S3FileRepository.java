package com.example.demo.repositories;


import com.example.demo.models.S3File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3FileRepository extends JpaRepository<S3File, Long> {
    S3File findByFileName(String fileUrl);
}
