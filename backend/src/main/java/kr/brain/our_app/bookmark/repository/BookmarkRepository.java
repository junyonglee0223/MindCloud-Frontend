package kr.brain.our_app.bookmark.repository;

import kr.brain.our_app.bookmark.dto.Bookmark;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookmarkRepository  {
    //일단은 hashmap해서 메모리저장, db 직접 저장은 다음에


    private static final Map<Long, Bookmark> store = new HashMap<>();
    private static long sequence = 0L;

    public Bookmark save(Bookmark bookmark) {
        bookmark.setId(++sequence);
        store.put(bookmark.getId(),bookmark);
        return bookmark;
    }

    public Bookmark findById(Long id) {
        return store.get(id);
    }

    public List<Bookmark> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long Id, Bookmark updateParam) {
        Bookmark findItem = findById(Id);
        findItem.setName(updateParam.getName());
        findItem.setTags(updateParam.getTags());
    }

    public void clearStore(){
        store.clear();
    }

}
