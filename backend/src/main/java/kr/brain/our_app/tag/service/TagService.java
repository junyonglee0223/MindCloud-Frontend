package kr.brain.our_app.tag.service;

import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.bookmark.repository.BookmarkRepository;
import kr.brain.our_app.tag.dto.Tag;
import kr.brain.our_app.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private TagRepository tagRepository;
    private BookmarkRepository bookmarkRepository;

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }
    public List<Bookmark> getBookmarkByTagname(String tagname) {
        return bookmarkRepository.findByTags_Tagname(tagname);
    }

//    public List<Bookmark> getBookmarkByTagname(String tagname){
//        List<Tag> tags = tagRepository.findByTagname(tagname);
//        return tags.stream()
//                .map(Tag::getBookmark)
//                .collect(Collectors.toList());
//    }

    //이 코드에 대해서 좀 더 고민해봐야 할것 같음.
    // 안 배운 게 많아서 좀 잘 이해가 안간다.
}
