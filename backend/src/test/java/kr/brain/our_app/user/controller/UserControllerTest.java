package kr.brain.our_app.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.brain.our_app.user.dto.User;
import kr.brain.our_app.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // 테스트 전에 DB 초기화
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@gmail.com"));
    }

    @Test
    public void testGetUserById() throws Exception{
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");
        userRepository.save(user);
        Long id = user.getId();
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@gmail.com"));
    }


    @Test
    public void testGetAllUsers() throws Exception {
        // 사용자 2명 추가
        User user1 = new User();
        user1.setUsername("testuser1");
        user1.setEmail("testuser1@gmail.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("testuser2");
        user2.setEmail("testuser2@gmail.com");
        userRepository.save(user2);

        // 모든 사용자 조회
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("testuser1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("testuser2"));
    }
}
