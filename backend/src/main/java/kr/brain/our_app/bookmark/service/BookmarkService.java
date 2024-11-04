package kr.brain.our_app.bookmark.service;

import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.dto.BookmarkDto;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.idsha.IDGenerator;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import kr.brain.our_app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository, UserService userService) {
        this.bookmarkRepository = bookmarkRepository;
        this.userService = userService;
    }

    // 1. 북마크 저장
    public BookmarkDto createBookmark(BookmarkDto bookmarkDto, UserDto userDto) {
        // 유저 ID로 User 객체 조회
        UserDto findUserDto = userService.findById(userDto.getId());

        //FIXME entity 변경 로직 추가 toEntity 메서드 추가 고려하는게 좋을 듯...
        User user = User.builder()
                .id(findUserDto.getId())
                .userName(findUserDto.getUserName())
                .email(findUserDto.getEmail())
                .build();

        String createbookmarkId = IDGenerator.generateId(bookmarkDto.getBookmarkName());
        //user 객체를 전달해서 setUser(user) 전달x

        Bookmark bookmark = new Bookmark();
        bookmark.setBookmarkName(bookmarkDto.getBookmarkName());
        bookmark.setUrl(bookmarkDto.getUrl());
        bookmark.setUser(user);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return BookmarkDto.builder()
                .bookmarkName(savedBookmark.getBookmarkName())
                .url(savedBookmark.getUrl())
                .build();
    }

    // 2. 북마크 전체 조회
    public List<BookmarkDto> findAllBookmarks(UserDto userDto) {
        return bookmarkRepository.findAllByUser_Id(userDto.getId())
                .stream()
                .map(bookmark -> BookmarkDto.builder()
                        .bookmarkName(bookmark.getBookmarkName())
                        .url(bookmark.getUrl())
                        .build())
                .collect(Collectors.toList());

    }

    // 이걸로 할거야 아니면 findByUser_Id(String user_id) 로 할거야?

    // 3. 이름으로 북마크 찾기
    public BookmarkDto findByBookmarkName(String bookmarkName) {
        return bookmarkRepository.findByBookmarkName(bookmarkName)
                .map(bookmark -> new BookmarkDto(
                        bookmark.getBookmarkName(),
                        bookmark.getUrl()
                )).orElseThrow(()->new IllegalArgumentException("해당 bookmark가 존재하지 않습니다 " +bookmarkName));
    }

    // bookmarkname과 UserId로 북마크 조회
    public BookmarkDto findBookmarkByBookmarkNameAndUserId(BookmarkDto bookmarkDto, UserDto userDto){
        return bookmarkRepository
                .findByBookmarkNameAndUser_Id(bookmarkDto.getBookmarkName(), userDto.getId())
                .map(bookmark -> new BookmarkDto(
                        bookmark.getBookmarkName(),
                        bookmark.getUrl()
                )).orElseThrow(()->new IllegalArgumentException("이 userId에 해당 bookmark가 존재하지 않습니다." + bookmarkDto.getBookmarkName()));
    } //bookmarkDto.getBookmarkName()이 제대로 출력되는지 살펴봐야한다.


    //  북마크 삭제
    public void deleteBookmark(String bookmarkId) {
        // 1. Bookmark ID로 객체 조회
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 Bookmark가 존재하지 않습니다."));

        // 2. Bookmark 객체 삭제
        bookmarkRepository.delete(bookmark);
    }

    // 5. 북마크 아이디로 북마크 조회
    public BookmarkDto findBookmarkById(String bookmarkId) {
        return bookmarkRepository.findById(bookmarkId) // Optional<Bookmark>를 반환하는 메서드 사용
                .map(bookmark -> new BookmarkDto(
                        bookmark.getId(),
                        bookmark.getUrl()
                )).orElseThrow(()-> new IllegalArgumentException("해당 ID를 가진 Bookmark를 찾을 수 없습니다."));
    }
}

