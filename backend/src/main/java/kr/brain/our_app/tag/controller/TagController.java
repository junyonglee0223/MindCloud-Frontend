package kr.brain.our_app.tag.controller;

import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.tag.dto.Tag;
import kr.brain.our_app.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;


    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.findAllTags();
        return ResponseEntity.ok(tags);
    }
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag savedTag = tagService.createTag(tag);
        return ResponseEntity.ok(savedTag);
    }

    //태그명으로 북마크 조회 여기 아무리 봐도 재설정 할 필요가 있다 이거 찾기 어렵다
    @GetMapping("/serach_all_bookmarks_by_tag")
    public ResponseEntity<List<Bookmark>> getBookmarkByTagname(@RequestParam String tagname) {
        List<Bookmark> bookmark = tagService.getBookmarkByTagname(tagname);
        if (bookmark.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookmark);
    }
    
    // 북마크에 어떤 태그가 있는지 조회 하는 것 필요
}
