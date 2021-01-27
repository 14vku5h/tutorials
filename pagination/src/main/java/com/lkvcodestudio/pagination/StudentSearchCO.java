package com.lkvcodestudio.pagination;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class StudentSearchCO {
    Optional<Integer> page;
    Optional<Integer> size;
    private Klass klass;
    private String dateOfAdmission;
    private String srchTxt;
}
