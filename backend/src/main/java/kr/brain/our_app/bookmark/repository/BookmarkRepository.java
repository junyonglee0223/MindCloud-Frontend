package kr.brain.our_app.bookmark.repository;

import kr.brain.our_app.bookmark.domain.Bookmark;
import kr.brain.our_app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface BookmarkRepository extends JpaRepository<Bookmark, String> {
//    jpa interface를 상속받아서 빈등록, 기초함수 구현을 하지않아도됨.
//    대신 repository도 class가 아닌 interface로 받아줘야함

    // 북마크 이름(name)으로 찾기, BOOKMARK는 LIST
    Optional<Bookmark> findByBookmarkName(String bookmarkName);
    List<Bookmark> findAllByUser_Id(String user_id);
    Optional<Bookmark> findByBookmarkNameAndUser_Id(String bookmarkName, String user_id);
}

// 여기서 Optional<Bookmark>를 받아서 service에서 BookmarkDto로 변환함
// Optional로 빈 객체인지 조회하고, bookmarkDto로 리턴해주기에 문제 없음
