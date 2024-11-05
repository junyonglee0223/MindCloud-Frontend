package kr.brain.our_app.tag.repository;

import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.tag.dto.TagDto;
import kr.brain.our_app.user.domain.User;
import kr.brain.our_app.user.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findByTagName(String tagName);
    //해당 메서드는 findAllByUser_Id에 의해 정의되기에, 리포지토리에서 굳이 재정의할필요x그래도 일단 만들어놓음
    List<Tag> findAllByUser_Id(String userId);
    boolean existsByTagName(String tagName);

}
