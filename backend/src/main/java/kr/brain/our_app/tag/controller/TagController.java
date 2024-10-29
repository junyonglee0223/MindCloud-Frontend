package kr.brain.our_app.tag.controller;

import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.dto.TagDto;
import kr.brain.our_app.tag.service.TagService;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public TagController(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto, @RequestParam String userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        TagDto savedTag = tagService.createTag(tagDto, user);
        return ResponseEntity.ok(savedTag);
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags(@RequestParam String userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<TagDto> tags = tagService.findAllTags(user);
        return ResponseEntity.ok(tags);
    }


//
//    // 2. tagname으로 tag 찾기
//    @GetMapping("/{tagname}")
//    public ResponseEntity<Tag> findByTagName(@PathVariable String tagName) {
//        // 서비스에서 태그명을 이용해 태그를 찾음
//        Tag tag = tagService.findByTagName(tagName);
//        return ResponseEntity.ok(tag);  // 찾은 태그를 200 OK로 반환
//    }
//
//    // 3. 모든 태그 출력
//    @GetMapping("/all")
//    public ResponseEntity<List<Tag>> findAllTags() {
//        // 서비스에서 모든 태그를 조회
//        List<Tag> tags = tagService.findAllTags();
//        return ResponseEntity.ok(tags);  // 조회된 태그 리스트를 200 OK로 반환
//    }
//
//    // 4. Tagid로 Tag 찾기
//    @GetMapping("/{tagid}")
//    public ResponseEntity<Tag> getTagById(@PathVariable Long tagId) {
//        Tag tag = tagService.getTagById(tagId);
//        return ResponseEntity.ok(tag);
//    }

}
