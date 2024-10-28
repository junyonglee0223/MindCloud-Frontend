package kr.brain.our_app.tag.service;

import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    public void setUp() {
        // Mock 객체 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTag() {
        // given
        Tag tag = new Tag("Java");
        when(tagRepository.save(tag)).thenReturn(tag);

        // when
        Tag createdTag = tagService.createTag(tag);

        // then
        assertNotNull(createdTag);
        assertEquals("Java", createdTag.getTagname());
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    public void testGetBookmarkByTagname() {
        // given
        String tagname = "Java";
        List<Bookmark> bookmarks = new ArrayList<>();
        when(bookmarkRepository.findByTags_Tag_Tagname(tagname)).thenReturn(bookmarks);

        // when
        List<Bookmark> result = tagService.getBookmarkByTagname(tagname);

        // then
        assertNotNull(result);
        verify(bookmarkRepository, times(1)).findByTags_Tag_Tagname(tagname);
    }
}