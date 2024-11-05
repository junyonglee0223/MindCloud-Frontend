package kr.brain.our_app.user.service;

import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .userName("TestUser")
                .email("test@example.com")
                .build();
    }
    @Test
    void testCreateUser() {
        // when
        UserDto createdUser = userService.createUser(userDto);

        // then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(createdUser.getUserName()).isEqualTo(userDto.getUserName());
    }

    @Test
    void testCreateUser_DuplicateEmail() {
        // given
        userService.createUser(userDto);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDto));
    }

    @Test
    void testFindAll() {
        // given
        userService.createUser(userDto);
        userService.createUser(UserDto.builder().userName("AnotherUser").email("another@example.com").build());

        // when
        List<UserDto> users = userService.findAll();

        // then
        assertThat(users).hasSize(2);
        assertThat(users).extracting("email").containsExactlyInAnyOrder("test@example.com", "another@example.com");
    }

    @Test
    void testFindById() {
        // given
        UserDto createdUser = userService.createUser(userDto);

        // when
        UserDto foundUser = userService.findById(createdUser.getId());

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(createdUser.getId());
        assertThat(foundUser.getEmail()).isEqualTo(createdUser.getEmail());
    }

    @Test
    void testFindById_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> userService.findById("nonexistent-id"));
    }

    @Test
    void testFindByEmail() {
        // given
        userService.createUser(userDto);

        // when
        UserDto foundUser = userService.findByEmail(userDto.getEmail());

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void testExistsByEmail() {
        // given
        userService.createUser(userDto);

        // when
        boolean exists = userService.existsByEmail(userDto.getEmail());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmail_NotFound() {
        // when
        boolean exists = userService.existsByEmail("nonexistent@example.com");

        // then
        assertThat(exists).isFalse();
    }

}