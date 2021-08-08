package com.lkvcodestudio.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Setter
@ToString
public class StudentSearchCO {
    Optional<Integer> page;
    Optional<Integer> size;
    private Klass klass;
    private String dateOfAdmission;
    private String srchTxt;
    private String orders;
}
