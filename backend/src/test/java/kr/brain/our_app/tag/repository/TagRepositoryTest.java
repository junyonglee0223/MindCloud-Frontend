package kr.brain.our_app.tag.repository;

import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.tag.dto.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TagRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void testCreateAndFindTagByTagname() {
        // Given: 북마크와 태그 생성
        Bookmark bookmark = new Bookmark();
        bookmark.setName("Another Bookmark");

        Tag tag = new Tag();
        tag.setTagname("Another Tag");
        tag.setBookmark(bookmark);

        bookmark.setTags(List.of(tag));

        // When: 북마크와 태그 저장
        bookmarkRepository.save(bookmark);
        tagRepository.save(tag);

        // Then: 태그명으로 조회하여 연결된 북마크 확인
        List<Tag> foundTags = tagRepository.findByTagname("Another Tag");

        assertThat(foundTags).isNotEmpty();
        assertThat(foundTags.get(0).getBookmark().getName()).isEqualTo("Another Bookmark");
    }
}