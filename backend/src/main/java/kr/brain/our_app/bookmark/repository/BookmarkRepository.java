package kr.brain.our_app.bookmark.repository;

import kr.brain.our_app.bookmark.dto.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
//    jpa interface를 상속받아서 빈등록, 기초함수 구현을 하지않아도됨.
//    대신 repository도 class가 아닌 interface로 받아줘야함

    // 북마크 이름(name)으로 찾기, BOOKMARK는 LIST
    List<Bookmark> findByBookmarkName(String bookmarkName);

    // 특정 태그를 가진 북마크 찾기 (다대다 조회),TAG로 북마크 찾음 -> LIST
    List<Bookmark> findByTags_Tag_Tagname(String tagName);
}
