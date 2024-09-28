package kr.brain.our_app.tag.repository;

import kr.brain.our_app.tag.dto.Tag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TagRepository {

    // 메모리 저장용 HashMap
    private static final Map<Long, Tag> store = new HashMap<>();
    private static long sequence = 0L;

    // Tag 저장
    public Tag save(Tag tag) {
        tag.setId(++sequence);
        store.put(tag.getId(), tag);
        return tag;
    }

    // ID로 Tag 조회
    public Tag findById(Long id) {
        return store.get(id);
    }

    // 모든 Tag 조회
    public List<Tag> findAll() {
        return new ArrayList<>(store.values());
    }

    // Tag 업데이트
    public void update(Long id, Tag updateParam) {
        Tag findTag = findById(id);
        findTag.setTagname(updateParam.getTagname());
    }

    // 저장소 초기화
    public void clearStore() {
        store.clear();
    }
}
