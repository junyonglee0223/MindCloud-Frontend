package kr.brain.our_app.tag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        tagRepository.deleteAll(); // 테스트 데이터 초기화
    }

    // 태그 생성 테스트
    @Test
    public void testCreateTag() throws Exception {
        Tag tag = new Tag("Java");

        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()) // 생성된 태그의 ID 확인
                .andExpect(jsonPath("$.tagname").value("Java"));
    }

    // ID로 태그 조회 테스트
    @Test
    public void testGetTagById() throws Exception {
        // 테스트 데이터 저장
        Tag tag = new Tag("Java");
        tagRepository.save(tag);
        Long id = tag.getId();

        mockMvc.perform(get("/tags/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagname").value("Java"));
    }

    // 모든 태그 조회 테스트
    @Test
    public void testGetAllTags() throws Exception {
        // 테스트 데이터 저장
        Tag tag1 = new Tag("Java");
        Tag tag2 = new Tag("Spring");
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tagname").value("Java"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].tagname").value("Spring"));
    }

    // 태그 삭제 테스트
    @Test
    public void testDeleteTag() throws Exception {
        // 테스트 데이터 저장
        Tag tag = new Tag("Java");
        tagRepository.save(tag);
        Long id = tag.getId();

        // 삭제 요청
        mockMvc.perform(delete("/tags/{id}", id))
                .andExpect(status().isNoContent());

        // 삭제 확인
        Optional<Tag> deletedTag = tagRepository.findById(id);
        assert(deletedTag.isEmpty()); // 태그가 삭제되었음을 확인
    }
}