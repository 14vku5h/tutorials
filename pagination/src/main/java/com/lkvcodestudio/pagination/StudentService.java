package com.lkvcodestudio.pagination;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class StudentService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private StudentRepository studentRepository;


    public Klass[] getKlasses(){
        return Klass.values();
    }

    public ResponseEntity getPaginatedStudents(StudentSearchCO srchCo) {
        try {

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
            Root root = criteriaQuery.from(Student.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (Strings.isNotBlank(srchCo.getSrchTxt())) {
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + srchCo.getSrchTxt() + "%"));
            }
            if (Strings.isNotBlank(srchCo.getDateOfAdmission())) {
                predicateList.add(criteriaBuilder.equal(root.get("dateOfAdmission"),Utils.convertStrToDate(srchCo.getDateOfAdmission(),"yyyy-MM-dd")));
            }
            if (srchCo.getKlass() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("klass"), srchCo.getKlass()));
            }

            List<Order> orderList = new ArrayList<>();
            String orderStr = srchCo.getOrders();
            if(!orderStr.isEmpty()){
                String order[] = orderStr.split("@@");
                for(int x=0;x<order.length;x++){
                    String s[] = order[x].split("::");
                    orderList.add(
                            s[1].matches("ASC")?
                                    criteriaBuilder.asc(root.get(s[0])) :
                                    criteriaBuilder.desc(root.get(s[0])));
                }
            }

            criteriaQuery.orderBy(orderList);

            Predicate[] predicateArr = new Predicate[predicateList.size()];
            Predicate predicate = criteriaBuilder.and(predicateList.toArray(predicateArr));
            criteriaQuery = criteriaQuery.where(predicate);
            TypedQuery<Student> typedQuery = entityManager.createQuery(criteriaQuery.select(root));
            List<Student> resultList = typedQuery.getResultList();
            List<StudentDTO> dtos = new ArrayList<>();
            resultList.forEach(r -> {
                dtos.add(new StudentDTO(r));
            });
            int pageSize = srchCo.getSize().orElse(10);
            int pageNumber = srchCo.getPage().orElse(1) - 1;
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "name");
            Page<StudentDTO> resultPage = null;

            if (resultList.size() > 0) {
                int from = pageNumber * pageSize;
                int to = from + pageSize;
                if (resultList.size() < to) {
                    to = resultList.size();
                }
                resultPage = new PageImpl<>(dtos.subList(from, to), pageable, dtos.size());
            } else resultPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }


    @PostConstruct
    public void booststrapSampleData(){
        studentRepository.deleteAll();
        if(studentRepository.count()==0) {
            List<Student> studentList = new ArrayList<>();
            List<Klass> klasses = Arrays.asList(getKlasses());
            List<String> names = Arrays.asList("Aman", "Sujeet", "Vipin kumar", "Lavkush Verma", "swapnil", "Sakshi", "Sanduana Siva");

            IntStream.range(0,klasses.size()).forEach(k -> {
                LocalDate date = LocalDate.now();
                IntStream.range(0,names.size()).forEach(n->{
                    studentList.add(new Student(names.get(n),names.get(n).toLowerCase()+k+n+"@demo.com",klasses.get(k),date.minusDays(k)));
                });
            });

            studentRepository.saveAll(studentList);
        }
    }
}
