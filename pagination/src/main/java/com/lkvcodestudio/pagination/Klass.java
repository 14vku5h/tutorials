package com.lkvcodestudio.pagination;

import lombok.Getter;

@Getter
public enum  Klass {
     SIXTH("Sixth"),
     SEVENTH("Seventh"),
     EIGHTH("Eighth"),
     NINTH("Ninth"),
     TENTH("Tenth"),
     ELEVENTH("Eleventh"),
     TWELFTH("Twelfth");

    private String std;

    Klass(String std){
        this.std=std;
    }

}
