package kr.brain.our_app.tag.service;

import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {

        this.tagRepository = tagRepository;
    }
    // 1. tag 생성
    public Tag createTag(Tag tag) {

        return tagRepository.save(tag);
    }

    // 2. tagname으로 tag 객체 찾기
    public Tag findByTagName(String tagName) {
        Optional<Tag> tag = tagRepository.findByTagName(tagName);

        // 태그가 존재하지 않으면 예외 발생
        return tag.orElseThrow(()
                -> new IllegalArgumentException("Tag not found with name: " + tagName));
    }

    // 3. 모든 태그 출력
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    // 4. tagID로 태그 조회
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + id));
    }

    // 5. tagName 으로 태그 조회
}
