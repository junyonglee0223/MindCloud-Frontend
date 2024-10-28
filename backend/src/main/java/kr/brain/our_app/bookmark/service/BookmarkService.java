package kr.brain.our_app.bookmark.service;

import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    // 1. 북마크 저장
    public Bookmark createBookmark(Bookmark bookmark) {
        if (bookmark.getUser() == null) {
            throw new IllegalArgumentException("Bookmark must have an associated User");
        }
        return bookmarkRepository.save(bookmark);
    }

    // 2. 북마크 전체 조회
    public List<Bookmark> getAllBookmark() {
        return bookmarkRepository.findAll();
    }

    // 3. 북마크 삭제
    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }

    // 4. 북마크 이름으로 북마크 조회
    public Optional<Bookmark> getBookmarkByName(String bookmarkName) {
        return bookmarkRepository.findByBookmarkName(bookmarkName);
    }

    // 5. 북마크 아이디로 북마크 조회
    public Bookmark getBookmarkById(Long id) {
        return bookmarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("bookmark not found with id: " + id));
    }

}

