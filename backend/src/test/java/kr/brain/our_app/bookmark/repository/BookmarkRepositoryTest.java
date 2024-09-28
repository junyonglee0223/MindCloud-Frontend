package kr.brain.our_app.bookmark.repository;

import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.tag.dto.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

class BookmarkRepositoryTest {

    BookmarkRepository bookmarkRepository;

    @BeforeEach
    void setUp() {
        bookmarkRepository = new BookmarkRepository();
    }

    @Test
    void save() {
        // given
        Bookmark bookmark = new Bookmark();
        bookmark.setName("Test Bookmark");

        // when
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        // then
        assertThat(savedBookmark).isNotNull();
        assertThat(savedBookmark.getId()).isGreaterThan(0);
        assertThat(savedBookmark.getName()).isEqualTo("Test Bookmark");
    }

    @Test
    void findById() {
        // given
        Bookmark bookmark = new Bookmark();
        bookmark.setName("Test Bookmark");
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        // when
        Bookmark foundBookmark = bookmarkRepository.findById(savedBookmark.getId());

        // then
        assertThat(foundBookmark).isNotNull();
        assertThat(foundBookmark.getId()).isEqualTo(savedBookmark.getId());
        assertThat(foundBookmark.getName()).isEqualTo("Test Bookmark");
    }

    @Test
    void findAll() {
        // given
        Bookmark bookmark1 = new Bookmark();
        bookmark1.setName("Bookmark 1");
        Bookmark bookmark2 = new Bookmark();
        bookmark2.setName("Bookmark 2");

        bookmarkRepository.save(bookmark1);
        bookmarkRepository.save(bookmark2);

        // when
        List<Bookmark> bookmarks = bookmarkRepository.findAll();

        // then
        assertThat(bookmarks).hasSize(2);
        assertThat(bookmarks).extracting(Bookmark::getName).containsExactly("Bookmark 1", "Bookmark 2");
    }

    @Test
    void update() {
        // given
        Bookmark bookmark = new Bookmark();
        bookmark.setName("Old Bookmark");
        bookmarkRepository.save(bookmark);

        // when
        Bookmark updateParam = new Bookmark();
        updateParam.setName("Updated Bookmark");
        bookmarkRepository.update(bookmark.getId(), updateParam);

        // then
        Bookmark updatedBookmark = bookmarkRepository.findById(bookmark.getId());
        assertThat(updatedBookmark.getName()).isEqualTo("Updated Bookmark");
    }

    @Test
    void clearStore() {
        // given
        Bookmark bookmark1 = new Bookmark();
        bookmark1.setName("Bookmark 1");
        Bookmark bookmark2 = new Bookmark();
        bookmark2.setName("Bookmark 2");

        bookmarkRepository.save(bookmark1);
        bookmarkRepository.save(bookmark2);

        // when
        bookmarkRepository.clearStore();

        // then
        List<Bookmark> bookmarks = bookmarkRepository.findAll();
        assertThat(bookmarks).isEmpty();
    }
}
