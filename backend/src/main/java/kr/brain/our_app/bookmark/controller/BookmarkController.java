package kr.brain.our_app.bookmark.controller;
import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService){

        this.bookmarkService = bookmarkService;
    }

    // 1. 북마크 생성
    @PostMapping
    public Bookmark createBookmark(@RequestBody Bookmark bookmark){

        return bookmarkService.createBookmark(bookmark);
    }
    // 2. 모든 북마크 조회
    @GetMapping
    public List<Bookmark> getAllBookmark(){

        return bookmarkService.getAllBookmark();
    }
    // 3. 이름으로 북마크 조회
    @GetMapping("/search")
    public Optional<Bookmark> getBookmarkByName(@RequestParam String bookmarkName){
        return bookmarkService.getBookmarkByName(bookmarkName);
    }
    //북마크 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id){
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }

}