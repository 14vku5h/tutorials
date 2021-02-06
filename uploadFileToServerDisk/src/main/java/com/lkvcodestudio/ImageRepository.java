package com.lkvcodestudio;


import com.lkvcodestudio.models.ImageGallary;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository  extends JpaRepository<ImageGallary,Long> {
}
