package kr.brain.our_app.bookmark.service;

import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.dto.BookmarkDto;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    // 1. 북마크 저장
    public BookmarkDto createBookmark(BookmarkDto bookmarkDto, User user) {
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
    public List<BookmarkDto> findAllBookmarks(User user) {
        return bookmarkRepository.findAllByUser(user)
                .stream()
                .map(bookmark -> BookmarkDto.builder()
                        .bookmarkName(bookmark.getBookmarkName())
                        .url(bookmark.getUrl())
                        .build())
                .collect(Collectors.toList());

    }

    // 3. BookmarkName으로 북마크 찾기
    public Optional<BookmarkDto> findByBookmarkName(String bookmarkName) {
        return bookmarkRepository.findByBookmarkName(bookmarkName)
                .map(bookmark -> new BookmarkDto(
                        bookmark.getBookmarkName(),
                        bookmark.getUrl()
                ));
    }


    // 4. 북마크 삭제
    public void deleteBookmark(String bookmarkId) {
        // 1. Bookmark ID로 객체 조회
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Bookmark가 존재하지 않습니다."));

        // 2. Bookmark 객체 삭제
        bookmarkRepository.delete(bookmark);
    } // 여기는 dto를 삭제하는게 아닌, 엔티티를 직접 삭제하는 것

    // 5. 북마크 아이디로 북마크 조회
    public Optional<BookmarkDto> findBookmarkById(String bookmarkId) {
        return bookmarkRepository.findById(bookmarkId) // Optional<Bookmark>를 반환하는 메서드 사용
                .map(bookmark -> new BookmarkDto(
                        bookmark.getId(),
                        bookmark.getUrl()
                ));
    }
}

