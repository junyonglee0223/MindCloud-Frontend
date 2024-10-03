package kr.brain.our_app.bookmark.dto;

import jakarta.persistence.*;
import kr.brain.our_app.tag.dto.Tag;
import kr.brain.our_app.user.dto.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Bookmark")
@Getter
@Setter

public class Bookmark {
    @Id
    private Long id;
    private String bookmarkname;

    // User와 일대일 관계 설정
    @OneToOne
    @MapsId // User의 ID를 이 Bookmark의 ID로 매핑
    @JoinColumn(name = "user_id") // Bookmark 테이블에 user_id FK 생성
    private User user;

    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tags;
}
