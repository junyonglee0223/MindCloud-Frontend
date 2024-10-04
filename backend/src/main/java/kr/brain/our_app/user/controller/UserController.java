package kr.brain.our_app.user.controller;

import kr.brain.our_app.user.dto.User;
import kr.brain.our_app.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // 이메일로 사용자 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }

    //id를 url에 노출시키는 것 방지해야 함 -- 수정 필수 -> /profile, requestParam 사용
    @GetMapping("/{id}")
    public ResponseEntity<User> findUser(@PathVariable Long id){
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<User> findAll(){
        return userService.findAll();
    }
}
