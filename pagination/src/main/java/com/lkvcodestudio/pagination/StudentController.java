package com.lkvcodestudio.pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping(value={"/","/students"})
    public String students(){
        return "students";
    }

    @PostMapping("/students")
    public ResponseEntity articles(StudentSearchCO srchCo){
        return studentService.getPaginatedStudents(srchCo);
    }
}
