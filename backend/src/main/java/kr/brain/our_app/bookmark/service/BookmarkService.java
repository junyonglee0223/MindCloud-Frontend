package kr.brain.our_app.bookmark.service;

import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.bookmark.dto.BookmarkCreateRequest;
import kr.brain.our_app.bookmark.dto.BookmarkResponse;
import kr.brain.our_app.bookmark.dto.TagBookmark;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.bookmark.repository.TagBookmarkRepository;
import kr.brain.our_app.tag.dto.Tag;
import kr.brain.our_app.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;
    private final TagBookmarkRepository tagBookmarkRepository;

    public BookmarkResponse createBookmark(BookmarkCreateRequest bookmarkCreateRequest) {
        Bookmark bookmark = new Bookmark(bookmarkCreateRequest.getUrl(),
                bookmarkCreateRequest.getUser(),
                bookmarkCreateRequest.getTitle());

        bookmarkRepository.save(bookmark);

        for (String tagName : bookmarkCreateRequest.getTags()) {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
                Tag newTag = new Tag(tagName);
                return tagRepository.save(newTag);
            });
            TagBookmark tagBookmark = new TagBookmark(tag, bookmark);
            tagBookmarkRepository.save(tagBookmark);
            bookmark.addTagBookmark(tagBookmark);
        }

        return BookmarkResponse.of(bookmark);
    }

    public List<BookmarkResponse> getAllBookmark() {
        return bookmarkRepository.findAll().stream()
                .map(BookmarkResponse::of)
                .collect(Collectors.toList());
    }

    public List<BookmarkResponse> getBookmarkByName(String bookmarkName) {
        return bookmarkRepository.findByBookmarkName(bookmarkName).stream()
                .map(BookmarkResponse::of)
                .collect(Collectors.toList());
    }

    public List<BookmarkResponse> getBookmarkByTagName(String tagName) {
        return bookmarkRepository.findByTags_Name(tagName).stream()
                .map(BookmarkResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }
}
