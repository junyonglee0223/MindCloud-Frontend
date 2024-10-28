package kr.brain.our_app.tag.controller;

import kr.brain.our_app.tag.domain.Tag;
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

    //1. 태그생성
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag savedTag = tagService.createTag(tag);
        return ResponseEntity.ok(savedTag);
    }

    // 2. tagname으로 tag 찾기
    @GetMapping("/{tagname}")
    public ResponseEntity<Tag> findByTagName(@PathVariable String tagName) {
        // 서비스에서 태그명을 이용해 태그를 찾음
        Tag tag = tagService.findByTagName(tagName);
        return ResponseEntity.ok(tag);  // 찾은 태그를 200 OK로 반환
    }

    // 3. 모든 태그 출력
    @GetMapping("/all")
    public ResponseEntity<List<Tag>> findAllTags() {
        // 서비스에서 모든 태그를 조회
        List<Tag> tags = tagService.findAllTags();
        return ResponseEntity.ok(tags);  // 조회된 태그 리스트를 200 OK로 반환
    }

    // 4. Tagid로 Tag 찾기
    @GetMapping("/{tagid}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long tagId) {
        Tag tag = tagService.getTagById(tagId);
        return ResponseEntity.ok(tag);
    }

}
