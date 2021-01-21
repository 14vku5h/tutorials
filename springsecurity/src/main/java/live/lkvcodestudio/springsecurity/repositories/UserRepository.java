package live.lkvcodestudio.springsecurity.repositories;

import live.lkvcodestudio.springsecurity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMobile(String mobile);
}
