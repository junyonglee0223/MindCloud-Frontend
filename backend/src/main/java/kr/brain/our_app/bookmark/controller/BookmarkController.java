package kr.brain.our_app.bookmark.controller;

import jakarta.persistence.Access;
import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.bookmark.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    //모든 북마크 조회
    @GetMapping
    public List<Bookmark> getAllBookmark(){
        return bookmarkService.getAllBookmark();
    }
    //북마크 생성
    @PostMapping
    public Bookmark createBookmark(@RequestBody Bookmark bookmark){
        return bookmarkService.createBookmark(bookmark);
    }
    //이름으로 북마크 조회
   @GetMapping("/search")
    public List<Bookmark> getBookmarkByName(@RequestParam String name){
        return bookmarkService.getBookmarkByName(name);
   }
   //태그로 북마크 조회
    @GetMapping("/tag")
    public List<Bookmark> getBookmarkByTag(@RequestParam String tag){
        return bookmarkService.getBookmarkByTagName(tag);
    }
    //북마크 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id){
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }








}
