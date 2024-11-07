package kr.brain.our_app.bookmark.service;

import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.dto.BookmarkDto;
import kr.brain.our_app.bookmark.dto.RequestFrontDto;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.bookmark.service.BookmarkService;
import kr.brain.our_app.idsha.IDGenerator;
import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.dto.TagDto;
import kr.brain.our_app.tag.service.TagService;
import kr.brain.our_app.bookmark.domain.TagBookmark;
import kr.brain.our_app.bookmark.dto.TagBookmarkDto;
import kr.brain.our_app.bookmark.repository.TagBookmarkRepository;
import kr.brain.our_app.bookmark.service.TagBookmarkService;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class TagBookmarkServiceTest {

    @Autowired
    private TagBookmarkService tagBookmarkService;

    @Autowired
    private TagService tagService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private TagBookmarkRepository tagBookmarkRepository;

    @Autowired
    private UserService userService;

    private String tagId;
    private String bookmarkId;
    private String userId;

    @BeforeEach
    void setUp() {
        // 필요한 데이터 준비
        UserDto userDto = UserDto.builder()
                .userName("test1")
                .email("ljyong3339@gmail.com")
                .build();

        userId = userService.createUser(userDto).getId();

        TagDto tagDto = TagDto.builder()
                .tagName("SampleTag")
                .build();
        tagId = tagService.createTag(tagDto, userDto).getId();

        BookmarkDto bookmarkDto = BookmarkDto.builder()
                .bookmarkName("SampleBookmark")
                .url("http://sample.com")
                .build();
        bookmarkId = bookmarkService.createBookmark(bookmarkDto, userDto).getId();
    }

    @Test
    void testRequestTagBookmark_CreatesNewTagsAndBookmark() {
        RequestFrontDto requestFrontDto = RequestFrontDto.builder()
                .email("ljyong3339@gmail.com")
                .userName("test1")
                .title("Sample Bookmark")
                .url("http://sample.com")
                .tags(Arrays.asList("tag1", "tag2"))
                .build();

        // When: TagBookmark 생성
        List<TagBookmarkDto> result = tagBookmarkService.requestTagBookmark(requestFrontDto);

        // Then: 결과 검증
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(requestFrontDto.getTags().size());

        // 추가 검증: User와 Bookmark 생성 여부 확인
        UserDto userDto = userService.findByEmail(requestFrontDto.getEmail());
        assertThat(userDto).isNotNull();
        assertThat(userDto.getUserName()).isEqualTo(requestFrontDto.getUserName());
    }

    @Test
    void testCreateTagBookmark_Success() {

        // given
        TagBookmarkDto tagBookmarkDto = tagBookmarkService.createTagBookmark(tagId, bookmarkId, userId);

        // when
        TagBookmark retrievedTagBookmark = tagBookmarkRepository.findByTagIdAndBookmarkId(tagId, bookmarkId).orElse(null);

        // then
        assertThat(retrievedTagBookmark).isNotNull();
        assertThat(retrievedTagBookmark.getTag().getId()).isEqualTo(tagId);
        assertThat(retrievedTagBookmark.getBookmark().getId()).isEqualTo(bookmarkId);
    }
//    @Test
//    void testCreateTagBookmark_AlreadyExists() {
//        // given: 첫 번째 TagBookmark 생성
//        tagBookmarkService.createTagBookmark(tagId, bookmarkId, userId);
//
//        // when & then: 동일한 TagBookmark를 생성하려고 하면 예외가 발생
//        assertThrows(IllegalArgumentException.class, () -> tagBookmarkService.createTagBookmark(tagId, bookmarkId, userId));
//    }
}
