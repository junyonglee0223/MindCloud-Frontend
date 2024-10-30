package kr.brain.our_app.bookmark.controller;

import kr.brain.our_app.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 북마크 생성
    @PostMapping
    public ResponseEntity<BookmarkResponse> createBookmark(@RequestBody @Valid BookmarkCreateRequest bookmarkCreateRequest) {
        BookmarkResponse bookmarkResponse = bookmarkService.createBookmark(bookmarkCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookmarkResponse);
    }

    // 모든 북마크 조회
    @GetMapping
    public List<BookmarkResponse> getAllBookmark() {
        return bookmarkService.getAllBookmark();
    }

    // 이름으로 북마크 조회
    @GetMapping("/search")
    public List<BookmarkResponse> getBookmarkByName(@RequestParam String bookmarkName) {
        return bookmarkService.getBookmarkByName(bookmarkName);
    }

    // 태그로 북마크 조회
    @GetMapping("/tag")
    public List<BookmarkResponse> getBookmarkByTag(@RequestParam String tag) {
        return bookmarkService.getBookmarkByTagName(tag);
    }

    // 북마크 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }
}
