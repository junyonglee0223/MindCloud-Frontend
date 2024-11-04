package kr.brain.our_app.tag.service;

import kr.brain.our_app.bookmark.dto.BookmarkDto;
import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.dto.TagDto;
import kr.brain.our_app.tag.repository.TagRepository;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final UserService userService;

    @Autowired
    public TagService(TagRepository tagRepository, UserService userService) {

        this.tagRepository = tagRepository;
        this.userService = userService;
    }
    // 1. tag 생성
    public TagDto createTag(TagDto tagDto, UserDto userDto) {
        // 유저 ID로 User 객체 조회
        UserDto findUserDto = userService.findById(userDto.getId());

        //FIXME entity 변경 로직 추가 toEntity 메서드 추가 고려하는게 좋을 듯...
        User user = User.builder()
                .id(findUserDto.getId())
                .userName(findUserDto.getUserName())
                .email(findUserDto.getEmail())
                .build();

        Tag tag = new Tag();
        tag.setTagName(tagDto.getTagName());
        tag.setUser(user);

        Tag savedTag = tagRepository.save(tag);
        return TagDto.builder()
                .tagName(savedTag.getTagName())
                .build();
    }

    // 2. 유저가 가지고 있는 Tag 모두 출력
    public List<TagDto> findAllTags(UserDto userDto) {
        return tagRepository.findAllByUser_Id(userDto.getId())
                .stream()
                .map(tag -> TagDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .collect(Collectors.toList());
    } // tag가 없을 경우... 빈 리스트가 출력되는지 확인해봐야함

    // 3. tagName 으로 태그 조회 -> tagdto에 tagname이 담겨서 전달
    public TagDto findByTagName(String tagName) {
        return tagRepository.findByTagName(tagName)
                .map(tag -> TagDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .orElseThrow(()->new IllegalArgumentException("TagName을 가진 Tag가 존재하지 않습니다." + tagName));
    }// Optional 제거하고, 그냥 TagDto로 수정.

    // 4. TagId로 태그 조회 -> tagdto에 tagname이 담겨서 전달
    public TagDto findById(String id) {
        return tagRepository.findById(id)
                .map(tag -> TagDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .orElseThrow(()->new IllegalArgumentException("해당 TagId를 가진 Tag가 존재하지 않습니다" + id));
    }
}