package kr.brain.our_app.tag.service;

import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.dto.TagDto;
import kr.brain.our_app.tag.repository.TagRepository;
import kr.brain.our_app.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {

        this.tagRepository = tagRepository;
    }
    // 1. tag 생성
    public TagDto createTag(TagDto tagDto, User user) {
        Tag tag = new Tag();
        tag.setTagName(tagDto.getTagName());
        tag.setUser(user);

        Tag savedTag = tagRepository.save(tag);
        return TagDto.builder()
                .tagName(savedTag.getTagName())
                .build();
    }

    public List<TagDto> findAllTags(User user) {
        return tagRepository.findAllByUser(user)
                .stream()
                .map(tag -> TagDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<TagDto> findByTagName(String tagName) {
        return tagRepository.findByTagName(tagName)
                .map(tag -> TagDto.builder()
                        .tagName(tag.getTagName())
                        .build());
    }



//
//    // 2. tagname으로 tag 객체 찾기
//    public Tag findByTagName(String tagName) {
//        Optional<Tag> tag = tagRepository.findByTagName(tagName);
//
//        // 태그가 존재하지 않으면 예외 발생
//        return tag.orElseThrow(()
//                -> new IllegalArgumentException("Tag not found with name: " + tagName));
//    }
//
//    // 3. 모든 태그 출력
//    public List<Tag> findAllTags() {
//        return tagRepository.findAll();
//    }
//
//    // 4. tagID로 태그 조회
//    public Tag getTagById(Long id) {
//        return tagRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + id));
//    }
//
//    // 5. tagName 으로 태그 조회
}
