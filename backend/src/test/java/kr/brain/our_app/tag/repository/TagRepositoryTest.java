package kr.brain.our_app.tag.repository;

import kr.brain.our_app.tag.dto.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

class TagRepositoryTest {

    TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository = new TagRepository();
    }

    @Test
    void save() {
        // given
        Tag tag = new Tag();
        tag.setTagname("Spring Boot");

        // when
        Tag savedTag = tagRepository.save(tag);

        // then
        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getId()).isGreaterThan(0);
        assertThat(savedTag.getTagname()).isEqualTo("Spring Boot");
    }

    @Test
    void findById() {
        // given
        Tag tag = new Tag();
        tag.setTagname("Java");
        Tag savedTag = tagRepository.save(tag);

        // when
        Tag foundTag = tagRepository.findById(savedTag.getId());

        // then
        assertThat(foundTag).isNotNull();
        assertThat(foundTag.getId()).isEqualTo(savedTag.getId());
        assertThat(foundTag.getTagname()).isEqualTo("Java");
    }

    @Test
    void findAll() {
        // given
        Tag tag1 = new Tag();
        tag1.setTagname("Java");

        Tag tag2 = new Tag();
        tag2.setTagname("Spring");

        tagRepository.save(tag1);
        tagRepository.save(tag2);

        // when
        List<Tag> tags = tagRepository.findAll();

        // then
        assertThat(tags).hasSize(2);
        assertThat(tags).extracting(Tag::getTagname).containsExactly("Java", "Spring");
    }

    @Test
    void update() {
        // given
        Tag tag = new Tag();
        tag.setTagname("Old Tag");
        Tag savedTag = tagRepository.save(tag);

        // when
        Tag updateParam = new Tag();
        updateParam.setTagname("Updated Tag");
        tagRepository.update(savedTag.getId(), updateParam);

        // then
        Tag updatedTag = tagRepository.findById(savedTag.getId());
        assertThat(updatedTag.getTagname()).isEqualTo("Updated Tag");
    }

    @Test
    void clearStore() {
        // given
        Tag tag1 = new Tag();
        tag1.setTagname("Tag 1");

        Tag tag2 = new Tag();
        tag2.setTagname("Tag 2");

        tagRepository.save(tag1);
        tagRepository.save(tag2);

        // when
        tagRepository.clearStore();

        // then
        List<Tag> tags = tagRepository.findAll();
        assertThat(tags).isEmpty();
    }
}
