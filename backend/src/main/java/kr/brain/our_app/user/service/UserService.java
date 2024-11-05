package kr.brain.our_app.user.service;

import kr.brain.our_app.idsha.IDGenerator;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public UserDto createUser(UserDto userDto){
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException("User with this email already exists.");
        }

        String createId = IDGenerator.generateId(userDto.getEmail());
        User user = User.builder()
                .id(createId)
                .userName(userDto.getUserName())
                .email(userDto.getEmail())
                .build();
        User createdUser = userRepository.save(user);
        return UserDto.builder()
                .id(createdUser.getId())
                .userName(createdUser.getUserName())
                .email(createdUser.getEmail())
                .build();
    }
    public List<UserDto> findAll(){
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(
                        user -> UserDto.builder()
                                .id(user.getId())
                                .userName(user.getUserName())
                                .email(user.getEmail())
                                .build()
                ).collect(Collectors.toList());
    }
    /**************************************************/
    public UserDto findById(String id){
        User findUser = userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return UserDto.builder()
                .id(findUser.getId())
                .userName(findUser.getUserName())
                .email(findUser.getEmail())
                .build();
    }

    public UserDto findByEmail(String email){
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);

        return UserDto.builder()
                .id(findUser.getId())
                .userName(findUser.getUserName())
                .email(findUser.getEmail())
                .build();
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public boolean existsById(String id){
        return userRepository.existsById(id);
    }
    /**************************************************/
}
