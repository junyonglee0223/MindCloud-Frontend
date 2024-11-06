package kr.brain.our_app.bookmark.service;

import kr.brain.our_app.OurAppApplication;
import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.dto.BookmarkDto;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.service.UserService;
import kr.brain.our_app.idsha.IDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OurAppApplication.class)
@Transactional
class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    private UserDto userDto;
    private List<BookmarkDto> bookmarkDtos;

    @BeforeEach
    void setUp() {
        // User 생성
        userDto = UserDto.builder()
                .id(IDGenerator.generateId("user1@example.com"))
                .userName("testUser")
                .email("user1@example.com")
                .build();
        userService.createUser(userDto);

        // BookmarkDto 리스트 초기화
        bookmarkDtos = List.of(
                new BookmarkDto(null, "Bookmark1", "http://example1.com"),
                new BookmarkDto(null, "Bookmark2", "http://example2.com"),
                new BookmarkDto(null, "Bookmark3", "http://example3.com"),
                new BookmarkDto(null, "Bookmark4", "http://example4.com"),
                new BookmarkDto(null, "Bookmark5", "http://example5.com")
        );
    }

    @Test
    void testCreateBookmark() {
        bookmarkDtos.forEach(bookmarkDto -> {
            BookmarkDto createdBookmark = bookmarkService.createBookmark(bookmarkDto, userDto);
            System.out.println("Created Bookmark: " + createdBookmark);

            assertThat(createdBookmark).isNotNull();
            assertThat(createdBookmark.getBookmarkName()).isEqualTo(bookmarkDto.getBookmarkName());

            Bookmark savedBookmark = bookmarkRepository.findByBookmarkNameAndUser_Id(createdBookmark.getBookmarkName(), userDto.getId())
                    .orElse(null);
            assertThat(savedBookmark).isNotNull();
            assertThat(savedBookmark.getBookmarkName()).isEqualTo(bookmarkDto.getBookmarkName());
        });
    }

    @Test
    void testFindAllBookmarks() {
        // 북마크 생성
        bookmarkDtos.forEach(bookmarkDto -> bookmarkService.createBookmark(bookmarkDto, userDto));

        // 모든 북마크 조회
        List<BookmarkDto> bookmarks = bookmarkService.findAllBookmarks(userDto);
        System.out.println("All Bookmarks: ");
        bookmarks.forEach(b -> System.out.println("Bookmark: " + b));

        assertThat(bookmarks).isNotNull();
        assertThat(bookmarks.size()).isEqualTo(5);
    }

    @Test
    void testFindByBookmarkName() {
        BookmarkDto bookmarkDto = bookmarkService.createBookmark(bookmarkDtos.get(0), userDto);
        BookmarkDto foundBookmark = bookmarkService.findByBookmarkName(bookmarkDto.getBookmarkName(), userDto);

        System.out.println("Found Bookmark by Name: " + foundBookmark);

        assertThat(foundBookmark).isNotNull();
        assertThat(foundBookmark.getBookmarkName()).isEqualTo(bookmarkDto.getBookmarkName());
    }

    @Test
    void testFindByBookmarkName_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            BookmarkDto notFoundBookmark = bookmarkService.findByBookmarkName("NonExistentBookmark", userDto);
            System.out.println("This bookmark should not be found: " + notFoundBookmark);
        });
    }

//    @Test
//    void testDeleteBookmark() {
//        BookmarkDto bookmarkDto = bookmarkService.createBookmark(bookmarkDtos.get(0), userDto);
//        assertThat(bookmarkDto.getId()).isNotNull();  // 생성된 북마크의 ID가 null이 아님을 확인
//
//        System.out.println("Deleting Bookmark with ID: " + bookmarkDto.getId());
//
//        bookmarkService.deleteBookmark(bookmarkDto.getId());
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            BookmarkDto deletedBookmark = bookmarkService.findBookmarkById(bookmarkDto.getId());
//            System.out.println("This bookmark should have been deleted: " + deletedBookmark);
//        });
//    }

    @Test
    void testFindBookmarkById() {
        BookmarkDto bookmarkDto = bookmarkService.createBookmark(bookmarkDtos.get(0), userDto);
        BookmarkDto foundBookmark = bookmarkService.findBookmarkById(bookmarkDto.getId());

        System.out.println("Found Bookmark by ID: " + foundBookmark);

        assertThat(foundBookmark).isNotNull();
        assertThat(foundBookmark.getBookmarkName()).isEqualTo(bookmarkDto.getBookmarkName());
    }

    @Test
    void testFindBookmarkById_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            BookmarkDto notFoundBookmark = bookmarkService.findBookmarkById("NonExistentId");
            System.out.println("This bookmark ID should not be found: " + notFoundBookmark);
        });
    }
}
