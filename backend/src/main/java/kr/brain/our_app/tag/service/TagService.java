package kr.brain.our_app.tag.service;

import kr.brain.our_app.bookmark.dto.BookmarkDto;
import kr.brain.our_app.idsha.IDGenerator;
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



        UserDto findUserDto = userService.findById(userDto.getId());
        User user = findUserDto.toEntity();

        if(tagRepository.existsByTagNameAndUser_Id(tagDto.getTagName(), userDto.getId())) {
            throw new IllegalArgumentException("This TagName already exists.");
        }
        //tag는 그냥 toentity() 안쓰고 만들어봄 field 를 알기 쉬우라고
        //TODO id 생성 시 tagName + user 속성 추가해서 생성해야 함
        String createId = IDGenerator.generateId(tagDto.getTagName()+user.getId());

        Tag tag = Tag.builder()
                .id(createId)
                .tagName(tagDto.getTagName())
                .user(user)
                .build();
        Tag savedTag = tagRepository.save(tag);

        return TagDto.builder()
                .id(savedTag.getId())
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

    // 3. tagName 으로 태그 조회 (userdto에 담긴 id로 사용자 식별)
    public TagDto findByTagName(String tagName, UserDto userDto) {
        return tagRepository.findByTagNameAndUser_Id(tagName, userDto.getId())
                .map(tag -> TagDto.builder()
                        .id(tag.getId())
                        .tagName(tag.getTagName())
                        .build())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 TagName을 가진 Tag가 존재하지 않습니다: " + tagName));
    }// Optional 제거하고, 그냥 TagDto로 수정.

    // 4. TagId로 태그 조회 -> tagdto에 tagname이 담겨서 전달
    public TagDto findById(String id) {
        return tagRepository.findById(id)
                .map(tag -> TagDto.builder()
                        .id(tag.getId())
                        .tagName(tag.getTagName())
                        .build())
                .orElseThrow(()->new IllegalArgumentException("해당 TagId를 가진 Tag가 존재하지 않습니다" + id));
    }
    public boolean existsById(String id){
        return tagRepository.existsById(id);
    }
    public boolean existsByTagName(String tagName, String userId){
        return tagRepository.existsByTagNameAndUser_Id(tagName, userId);
    }
}