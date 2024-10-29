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

    public Optional<BookmarkDto> findByBookmarkName(String bookmarkName) {
        return bookmarkRepository.findByBookmarkName(bookmarkName)
                .map(bookmark -> new BookmarkDto(
                        bookmark.getBookmarkName(),
                        bookmark.getUrl()
                ));
    }
//
//    // 3. 북마크 삭제
//    public void deleteBookmark(Long id) {
//        bookmarkRepository.deleteById(id);
//    }
//
//    // 4. 북마크 이름으로 북마크 조회
//    public Optional<Bookmark> getBookmarkByName(String bookmarkName) {
//        return bookmarkRepository.findByBookmarkName(bookmarkName);
//    }
//
//    // 5. 북마크 아이디로 북마크 조회
//    public Bookmark getBookmarkById(Long id) {
//        return bookmarkRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("bookmark not found with id: " + id));
//    }

}

