package com.lkvcodestudio.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ImageGallary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;

    public ImageGallary(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
