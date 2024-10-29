package kr.brain.our_app.tag.repository;

import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(String tagName);
    List<Tag> findAllByUser(Optional<User> user);

}
