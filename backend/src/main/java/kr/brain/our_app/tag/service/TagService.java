package kr.brain.our_app.tag.service;

import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.dto.TagDto;
import kr.brain.our_app.tag.repository.TagRepository;
import kr.brain.our_app.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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


    // 2. 유저가 가지고 있는 Tag 모두 출력
    public List<TagDto> findAllTags(User user) {
        return tagRepository.findAllByUser(user)
                .stream()
                .map(tag -> TagDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .collect(Collectors.toList());


    // 3. tagName 으로 태그 조회 -> tagdto에 tagname이 담겨서 전달
    public Optional<TagDto> findByTagName(String tagName) {
        return tagRepository.findByTagName(tagName)
                .map(tag -> TagDto.builder()
                        .tagName(tag.getTagName())
                        .build());

    // 4. TagId로 태그 조회 -> tagdto에 tagname이 담겨서 전달
    public Optional<TagDto> findById(String id) {
        return tagRepository.findById(id)
                .map(tag -> TagDto.builder()
                        .tagName(tag.getTagName())
                        .build());
    }

}
