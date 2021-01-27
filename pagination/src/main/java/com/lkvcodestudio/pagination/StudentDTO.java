package com.lkvcodestudio.pagination;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private String email;
    private String klass;
    private String dateOfAdmission;

    public StudentDTO(Student r) {
       this.id =r.getId();
       this.name=r.getName();
       this.email=r.getEmail();
       this.klass= r.getKlass().getStd();
       this.dateOfAdmission = Utils.convertDateToStr(r.getDateOfAdmission(),"dd/MM/yyyy");
    }
}
