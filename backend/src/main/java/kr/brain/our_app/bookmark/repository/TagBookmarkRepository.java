package kr.brain.our_app.bookmark.repository;

import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.bookmark.dto.TagBookmark;
import kr.brain.our_app.tag.dto.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//Page 객체를 사용할 떄, 위 2개를 사용하지 않고 딴거 사용하면 오류난다.
import java.util.Optional;

public interface TagBookmarkRepository extends JpaRepository<TagBookmark, Long>  {

    //optional 은 null이 될 가능성이 있을 때 사용, tag와 bookmark가 조합이 없을 경우가 있을수 있기에 사용
    Optional<TagBookmark> findByTagAndBookmark(final Tag tag, final Bookmark bookmark);

    //Page 객체는 찾은 데이터를 페이지 형태로 포함하며, 현재P,전체P, 페이지 크기 제공
    //Pageable 파라미터는 페이질 처리에 필요한 정보
    //(몇번쨰 페이지, 페이지당 몇개의 항목을 보여줄 것인지 등)을 포함
    Page<TagBookmark> findAllByTag(final Tag tag, final Pageable pageable);

    void deleteAllByTagId(final Long tagId);

}
