package live.lkvcodestudio.springsecurity.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String uniqueId = UUID.randomUUID().toString();
    String name;
    @Column(unique = true)
    String email;
    @Column(unique = true)
    String mobile;
    String password;
    String role;
    String profilePic;
    LocalDateTime createdOn;
    LocalDateTime updatedOn;

    @PrePersist
    public void creationSpot(){
        this.createdOn=LocalDateTime.now();
        this.updatedOn=LocalDateTime.now();
    }
    @PreUpdate
    public void updationSpot(){
        this.updatedOn=LocalDateTime.now();
    }

}
