package kr.brain.our_app.user.controller;

import kr.brain.our_app.idsha.IDGenerator;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.OAuthUserDto;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody OAuthUserDto oAuthUserDto){

        String userId = IDGenerator.generateId(oAuthUserDto.getEmail());
        User user = User.builder()
                .id(userId)
                .userName(oAuthUserDto.getUserName())
                .email(oAuthUserDto.getEmail())  // 이메일 설정 수정
                .build();

        userService.createUser(user);

        UserDto userDto = UserDto.builder()
                        .id(userId)
                        .userName(oAuthUserDto.getUserName())
                        .email(oAuthUserDto.getEmail())
                        .build();

        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>>findAllUsers(){
        List<User> userList = userService.findAll();
        List<UserDto>userDtoList = new ArrayList<>();
        for(User user : userList){
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .userName(user.getUserName())
                    .email(user.getEmail())
                    .build();
            userDtoList.add(userDto);
        }
        return ResponseEntity.ok(userDtoList);
    }

    /*********************************************************************/
    /*
    * 아래는 수정해야 할 코드
    *
    * */
    /*********************************************************************/
//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User user){
//        User createdUser = userService.createUser(user);
//        return ResponseEntity.ok(createdUser);
//    }
//
//    // 이메일로 사용자 정보 조회
//    @GetMapping("/profile")
//    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
//        Optional<User> user = userService.findByEmail(email);
//        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
//    }
//
//    //id를 url에 노출시키는 것 방지해야 함 -- 수정 필수 -> /profile, requestParam 사용
//    @GetMapping("/{id}")
//    public ResponseEntity<User> findUser(@PathVariable Long id){
//        return userService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/all")
//    public List<User> findAll(){
//        return userService.findAll();
//    }
}
