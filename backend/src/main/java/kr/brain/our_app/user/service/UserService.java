package kr.brain.our_app.user.service;

import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User createUser(User user){
        return userRepository.save(user);
    }
    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }
    /**************************************************/
    public UserDto findByEmail(String email){
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);

        return UserDto.builder()
                .id(findUser.getId())
                .userName(findUser.getUserName())
                .email(findUser.getEmail())
                .build();
    }
    /**************************************************/
    public List<User> findAll(){
        return userRepository.findAll();
    }
}
