package kr.brain.our_app.bookmark.controller;
import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.dto.BookmarkDto;
import kr.brain.our_app.bookmark.service.BookmarkService;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserService userService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService, UserService userService){

        this.bookmarkService = bookmarkService;
        this.userService = userService;
    }

    // 1. 북마크 생성
    @PostMapping

    public ResponseEntity<BookmarkDto> createBookmark(@RequestBody BookmarkDto bookmarkDto,
                                                      @RequestParam String userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BookmarkDto savedBookmark = bookmarkService.createBookmark(bookmarkDto, user);
        return ResponseEntity.ok(savedBookmark);
    }
        // 2. 모든 북마크 조회
        public ResponseEntity<List<BookmarkDto>> getAllBookmark (@RequestParam String userId) {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            List<BookmarkDto> bookmarks = bookmarkService.findAllBookmarks(user);
            return ResponseEntity.ok(bookmarks);
        }
//    // 3. 이름으로 북마크 조회
//    @GetMapping("/search")
//    public Optional<Bookmark> getBookmarkByName(@RequestParam String bookmarkName){
//        return bookmarkService.getBookmarkByName(bookmarkName);
//    }
//    //북마크 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id){
//        bookmarkService.deleteBookmark(id);
//        return ResponseEntity.noContent().build();
//    }
}