package kr.brain.our_app.user.repository;

import kr.brain.our_app.user.dto.User;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>findById(Long id);
    /**************************************************/
    Optional<User>findByEmail(String email);
    /**************************************************/
}
