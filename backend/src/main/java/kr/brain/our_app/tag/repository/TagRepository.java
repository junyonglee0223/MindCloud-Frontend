package kr.brain.our_app.tag.repository;

import kr.brain.our_app.tag.dto.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, String> {

    List<Tag> findByTagname(String tagname);

}
