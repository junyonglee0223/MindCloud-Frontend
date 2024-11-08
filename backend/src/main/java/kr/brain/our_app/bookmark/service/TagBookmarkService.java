package kr.brain.our_app.bookmark.service;

import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.domain.TagBookmark;
import kr.brain.our_app.bookmark.dto.BookmarkDto;
import kr.brain.our_app.bookmark.dto.RequestFrontDto;
import kr.brain.our_app.bookmark.dto.TagBookmarkDto;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.bookmark.repository.TagBookmarkRepository;
import kr.brain.our_app.idsha.IDGenerator;
import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.dto.TagDto;
import kr.brain.our_app.tag.repository.TagRepository;
import kr.brain.our_app.tag.service.TagService;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagBookmarkService {

    private final TagBookmarkRepository tagBookmarkRepository;

    private final BookmarkService bookmarkService;
    private final TagService tagService;
    private final UserService userService;


    @Autowired
    public TagBookmarkService(TagBookmarkRepository tagBookmarkRepository,
                              BookmarkService bookmarkService,
                              TagService tagService,

                              UserService userService,

                              TagRepository tagRepository,
                              BookmarkRepository bookmarkRepository) {
        this.tagBookmarkRepository = tagBookmarkRepository;
        this.bookmarkService = bookmarkService;
        this.tagService = tagService;

        this.userService = userService;
    }


    public List<TagBookmarkDto> requestTagBookmark(RequestFrontDto requestFrontDto){
        System.out.println(requestFrontDto);    //TEST

        //WARN 해당 코드 처리하는 방식 나중에 user에서 처리하는 과정 필요함
        if(!userService.existsByEmail(requestFrontDto.getEmail())){
            UserDto createUserDto = UserDto.builder()
                    .id("makeKey")
                    .userName(requestFrontDto.getUserName())
                    .email(requestFrontDto.getEmail())
                    .build();
            userService.createUser(createUserDto);
        }

        UserDto userDto = userService.findByEmail(requestFrontDto.getEmail());

        String title = requestFrontDto.getTitle();
        String url = requestFrontDto.getUrl();

        //no id bookmarkdto
        BookmarkDto bookmarkDto = BookmarkDto.builder()
                .bookmarkName(requestFrontDto.getTitle())
                .url(requestFrontDto.getUrl())
                .build();

        if(!bookmarkService.existsByBookmarkName(title, userDto.getId())){
            bookmarkService.createBookmark(bookmarkDto, userDto);
        }

        BookmarkDto checkedBookmarkDto
                = bookmarkService
                .findByBookmarkName(bookmarkDto.getBookmarkName(),userDto);

        List<TagBookmarkDto>tagBookmarkDtoList = new ArrayList<>();

        for(String tagName : requestFrontDto.getTags()){
            TagDto tagDto = TagDto.builder()
                    .tagName(tagName)
                    .build();
            if(!tagService.existsByTagName(tagName, userDto.getId())){
                tagService.createTag(tagDto, userDto);
            }
            TagDto checkedTagDto
                    = tagService.findByTagName(tagName, userDto);
//                    .orElseThrow(IllegalArgumentException::new);
            //service 내에서 예외처리하면서 삭제함
            //entity check -> create dto
            //check logic is impied in other method
            if(!existsByTagIdAndBookmarkId(checkedTagDto.getId(), checkedBookmarkDto.getId())){
                createTagBookmark(checkedTagDto.getId(), checkedBookmarkDto.getId(), userDto.getId());
            }

            TagBookmarkDto checkedTagBookmarkDto =
                    findTagBookmarkByTagAndBookmark(checkedTagDto, checkedBookmarkDto);

            tagBookmarkDtoList.add(checkedTagBookmarkDto);
        }
        return tagBookmarkDtoList;
    }
    
    
    //tag 요청했을 경우 bookmarkDto 반환하는 서비스 로직
    public List<BookmarkDto> responseTagBookmark(String tagName, String userEmail){
        String userID = userService.findByEmail(userEmail).getId();

        //FIXME user dto로 받아야 하는지?? -> service가 dto로 받고 있는 상황 
        // -> 나중에 string 입력받는 방식으로 수정해야 함 
        
        UserDto userDto = UserDto.builder()
                .id(userID).build();
        String tagId = tagService.findByTagName(tagName, userDto).getId();
        List<TagBookmarkDto> tagBookmarkDtos = findByTagId(tagId, userID);

        List<BookmarkDto> bookmarkDtos = new ArrayList<>();
        for(TagBookmarkDto tbd : tagBookmarkDtos){
            BookmarkDto bookmarkDto = bookmarkService.findBookmarkById(tbd.getBookmarkId());
            bookmarkDtos.add(bookmarkDto);
        }
        return bookmarkDtos;
    }


    public TagBookmarkDto findTagBookmarkByTagAndBookmark(
            TagDto tagDto, BookmarkDto bookmarkDto
    ){
        if(!tagService.existsById(tagDto.getId()) || !bookmarkService.existsById(bookmarkDto.getId())){
            throw new IllegalArgumentException("Tag or Bookmark does not exist");
        }

        return tagBookmarkRepository
                .findByTagIdAndBookmarkId(tagDto.getId(), bookmarkDto.getId())
                .map(tagBookmark -> TagBookmarkDto.builder()
                        .tagId(tagDto.getId())
                        .bookmarkId(bookmarkDto.getId())
                        .build())
                .orElseThrow(null);
    }

    public TagBookmarkDto createTagBookmark(String tagId, String bookmarkId, String userId){
        //FIXME user entity 만드는 부분 여기에 구현해놨음
        // 1. dto에 user_id를 추가해서 통일성을 맞춘다.   -> 통일성 맞춤
        // 2. tagbookmark 생성시 userDto를 지속적으로 입력한다. -> 전체 refactoring 필요
        // 3. create 하는 부분에서만 user entity 필요함 find 영역에서는 필요없음
        UserDto currentUserDto = userService.findById(userId);

        User user = User.builder()
                .id(currentUserDto.getId())
                .userName(currentUserDto.getUserName())
                .email(currentUserDto.getEmail())
                .build();


        //TODO 이미 존재하면 예외처리
        String createTagBookmarkId
                = IDGenerator.generateId(tagId + bookmarkId);
        //TODO 새로 생성해야 함
        TagDto tagDto = tagService.findById(tagId);
        //TODO user entity를 입력해 줘야 하는 문제 있음
        //FIXME user 임시 추가
        Tag tag = Tag.builder()
                .id(tagDto.getId())
                .tagName(tagDto.getTagName())
                .user(user)
                .build();

        //TODO set 설정 관련 문제 해결해야 함
        BookmarkDto bookmarkDto = bookmarkService.findBookmarkById(bookmarkId);
        //TODO user entity를 입력해 주야 하는 문제 있음
        //FIXME user 임시 추가
        Bookmark bookmark = Bookmark.builder()
                .id(bookmarkDto.getId())
                .bookmarkName(bookmarkDto.getBookmarkName())
                .url(bookmarkDto.getUrl())
                .user(user)
                .build();

        TagBookmark tagBookmark = TagBookmark.builder()
                .id(createTagBookmarkId)
                .tag(tag)
                .bookmark(bookmark).build();


        TagBookmark savedTagBookmark = tagBookmarkRepository.save(tagBookmark);

        return TagBookmarkDto.builder()
                .tagId(savedTagBookmark.getTag().getId())
                .bookmarkId(savedTagBookmark.getBookmark().getId())
                .build();
    }

    //FIXME userId로 처리하는 부분 refactoring 필요
    List<TagBookmarkDto> findByTagId(String tagId, String userId){
        return tagBookmarkRepository.findByTagId(tagId).stream()
                .map(tagBookmark -> TagBookmarkDto.builder()
                        .tagId(tagBookmark.getTag().getId())
                        .bookmarkId(tagBookmark.getBookmark().getId())
                        .build())
                .collect(Collectors.toList());
    }

    public boolean existsByTagIdAndBookmarkId(String tagId, String bookmarkId){
        return tagBookmarkRepository.existsByTagIdAndBookmarkId(tagId, bookmarkId);
    }

//
//    public List<TagBookmarkDto> findAllByBookmark(BookmarkDto bookmarkDto) {
//        // BookmarkDto의 정보를 바탕으로 Bookmark 엔티티 조회
//        Bookmark bookmark = bookmarkRepository.findByBookmarkName(bookmarkDto.getBookmarkName())
//                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found"));
//
//        return tagBookmarkRepository.findAllByBookmark(bookmark)
//                .stream()
//                .map(tagBookmark -> TagBookmarkDto.builder()
//                        .tagName(tagBookmark.getTag().getTagName())
//                        .bookmarkName(tagBookmark.getBookmark().getBookmarkName())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    public List<TagBookmarkDto> findAllByTag(TagDto tagDto) {
//        // TagDto의 정보를 바탕으로 Tag 엔티티 조회
//        Tag tag = tagRepository.findByTagName(tagDto.getTagName())
//                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
//
//        return tagBookmarkRepository.findAllByTag(tag)
//                .stream()
//                .map(tagBookmark -> TagBookmarkDto.builder()
//                        .tagName(tagBookmark.getTag().getTagName())
//                        .bookmarkName(tagBookmark.getBookmark().getBookmarkName())
//                        .build())
//                .collect(Collectors.toList());
//    }

//    // 2. TagId로 연결된 모든 Bookmark 조회
//    public List<Bookmark> getBookmarksByTagId(Long tagId) {
//        Tag tag = tagRepository.findById(tagId)
//                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
//
//        List<TagBookmark> tagBookmarks = tagBookmarkRepository.findByTag(tag);
//
//        // 각 TagBookmark에서 Bookmark만 추출하여 반환
//        return tagBookmarks.stream()
//                .map(TagBookmark::getBookmark)
//                .collect(java.util.stream.Collectors.toList());
//    }
//
//    // 3. TagName으로 연결된 모든 Bookmark 조회
//    public List<Bookmark> getBookmarksByTagName(String tagName) {
//        Optional<Tag> tag = tagRepository.findByTagName(tagName); // 단일 Tag 반환
//
//        if (tag == null) {
//            throw new IllegalArgumentException("해당 TagName을 가진 Tag 객체가 존재하지 않음");
//        }
//
//    }

//    // 4. BookmarkId로 연결된 모든 Tag 조회
//    public List<Tag> getTagsByBookmarkId(Long bookmarkId) {
//        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
//                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found"));
//
//        List<TagBookmark> tagBookmarks = tagBookmarkRepository.findByBookmark(bookmark);
//
//        // 각 TagBookmark에서 Tag만 추출하여 반환
//        return tagBookmarks.stream()
//                .map(TagBookmark::getTag)
//                .collect(java.util.stream.Collectors.toList());
//    }
//
//    // 5. BookmarkName으로 연결된 모든 Tag 조회
//    public List<Tag> getTagsByBookmarkName(String bookmarkName) {
//        // BookmarkRepository에서 List<Bookmark>가 반환된다고 가정
//        List<Bookmark> bookmarks = bookmarkRepository.findByBookmarkName(bookmarkName);
//
//        // 빈 리스트일 경우 예외 처리
//        if (bookmarks.isEmpty()) {
//            throw new IllegalArgumentException("Bookmark not found");
//        }
//
//        // 첫 번째 북마크의 ID를 사용해 태그를 조회
//        Bookmark bookmark = bookmarks.get(0); // 만약 여러 북마크가 있으면 첫 번째만 사용 (필요시 수정)
//        return getTagsByBookmark(bookmark.getId());
//    }


//    // 태그 이름으로 북마크 가져오기
//    public List<Bookmark> getBookmarksByTagName(String tagName) {
//        // 태그명을 통해 태그를 찾음
//        Tag tag = tagRepository.findByTagName(tagName).orElseThrow(()
//                -> new IllegalArgumentException("Tag not found with name: " + tagName));
//
//        // 해당 태그로 연결된 TagBookmark에서 북마크를 가져옴
//        List<TagBookmark> tagBookmarks = tagBookmarkRepository.findByTag(tag);
//
//        // TagBookmark에서 Bookmark 객체들을 추출하여 반환
//        return tagBookmarks.stream()
//                .map(TagBookmark::getBookmark)
//                .collect(Collectors.toList());
//    }
}
