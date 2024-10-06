package kr.brain.our_app.bookmark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.bookmark.dto.TagBookmark;
import kr.brain.our_app.bookmark.service.TagBookmarkService;
import kr.brain.our_app.tag.dto.Tag;
import kr.brain.our_app.tag.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TagBookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagBookmarkService tagBookmarkService;

    @MockBean
    private TagRepository tagRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddBookmarkToTag() throws Exception {
        Tag tag = new Tag("Java");
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("http://example.com");
        TagBookmark tagBookmark = new TagBookmark(tag, bookmark);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagBookmarkService.createTagBookmark(tag, bookmark)).thenReturn(tagBookmark);

        mockMvc.perform(post("/api/bookmarks/tags")
                        .param("tagId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookmark)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag.tagname").value("Java"))
                .andExpect(jsonPath("$.bookmark.url").value("http://example.com"));
    }

    @Test
    public void testDeleteAllTagBookmarks() throws Exception {
        // Here we mock a void method with doNothing().
        doNothing().when(tagBookmarkService).deleteAllByTagId(1L);

        mockMvc.perform(delete("/api/bookmarks/tags/1/all-bookmarks"))
                .andExpect(status().isNoContent());
    }
}