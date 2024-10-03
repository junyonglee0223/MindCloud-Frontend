package kr.brain.our_app.tag.repository;

import kr.brain.our_app.tag.dto.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByTagname(String tagname);

}
