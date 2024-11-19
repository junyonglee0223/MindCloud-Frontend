package kr.brain.our_app.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.OAuthUserDto;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception {
        // given
        OAuthUserDto oAuthUserDto = OAuthUserDto.builder()
                .authId("auth123")
                .userName("Test User")
                .email("testuser@example.com")
                .build();

        // when
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(oAuthUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userName").value("Test User"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("response = "+jsonResponse);

        // then
        String responseContent = result.getResponse().getContentAsString();
        UserDto createdUser = objectMapper.readValue(responseContent, UserDto.class);
        User savedUser = userRepository.findById(createdUser.getId()).orElse(null);
        assert savedUser != null;
        assert savedUser.getUserName().equals("Test User");
        assert savedUser.getEmail().equals("testuser@example.com");
    }

    @Test
    public void testFindAllUsers() throws Exception {
        // given
        User user1 = User.builder().id("1").userName("User One").email("userone@example.com").build();
        User user2 = User.builder().id("2").userName("User Two").email("usertwo@example.com").build();
        userRepository.save(user1);
        userRepository.save(user2);

        // when
        MvcResult result = mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("User One"))
                .andExpect(jsonPath("$[1].userName").value("User Two"))
                .andExpect(jsonPath("$[0].email").value("userone@example.com"))
                .andExpect(jsonPath("$[1].email").value("usertwo@example.com"))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("!!!!!!!!! result = " + jsonResponse);
    }

//    @BeforeEach
//    public void setUp() {
//        userRepository.deleteAll(); // 테스트 전에 DB 초기화
//    }
//
//    @Test
//    public void testCreateUser() throws Exception {
//        User user = new User();
//        user.setUsername("testuser");
//        user.setEmail("testuser@gmail.com");
//
//        mockMvc.perform(post("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.email").value("testuser@gmail.com"));
//    }
//
//    @Test
//    public void testGetUserById() throws Exception{
//        User user = new User();
//        user.setUsername("testuser");
//        user.setEmail("testuser@gmail.com");
//        userRepository.save(user);
//        Long id = user.getId();
//        mockMvc.perform(get("/api/users/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.email").value("testuser@gmail.com"));
//    }
//
//
//    @Test
//    public void testGetAllUsers() throws Exception {
//        // 사용자 2명 추가
//        User user1 = new User();
//        user1.setUsername("testuser1");
//        user1.setEmail("testuser1@gmail.com");
//        userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setUsername("testuser2");
//        user2.setEmail("testuser2@gmail.com");
//        userRepository.save(user2);
//
//        // 모든 사용자 조회
//        mockMvc.perform(get("/api/users/all"))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("testuser1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("testuser2"));
//    }
}
