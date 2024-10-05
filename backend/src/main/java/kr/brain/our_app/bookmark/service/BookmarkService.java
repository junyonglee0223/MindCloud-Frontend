package kr.brain.our_app.bookmark.service;

import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.user.dto.User;
import kr.brain.our_app.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    // 북마크 저장
    public Bookmark createBookmark(Bookmark bookmark) {
        if (bookmark.getUser() == null) {
            throw new IllegalArgumentException("Bookmark must have an associated User");
        }
        return bookmarkRepository.save(bookmark);
    }

    // 북마크 전체 조회
    public List<Bookmark> getAllBookmark() {
        return bookmarkRepository.findAll();
    }

    // 북마크 삭제
    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }

    // 이름으로 북마크 조회
    public List<Bookmark> getBookmarkByName(String bookmarkName) {
        return bookmarkRepository.findByName(bookmarkName);
    }

    // 태그 이름으로 북마크 조회
    public List<Bookmark> getBookmarkByTagName(String tagName) {
        return bookmarkRepository.findByTags_Tagname(tagName);
    }


}
