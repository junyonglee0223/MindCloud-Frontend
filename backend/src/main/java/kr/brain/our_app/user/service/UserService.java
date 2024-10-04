package kr.brain.our_app.user.service;

import kr.brain.our_app.user.dto.User;
import kr.brain.our_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Transactional
    public User createUser(User user){
        return userRepository.save(user);
    }
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }
    /**************************************************/
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    /**************************************************/
    public List<User> findAll(){
        return userRepository.findAll();
    }
}
