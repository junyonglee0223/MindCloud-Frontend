package kr.brain.our_app.tag.dto;

import jakarta.persistence.*;
import kr.brain.our_app.user.dto.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name ="Tag")
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="tag_id")
    private String id;

    @NotEmpty
    @Column(nullable = false, length = 25)
    private String tagname;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Tag() {
        // 기본 생성자는 아무 동작을 하지 않거나 초기값을 설정 할 수 있음
    }


    @Builder
    public Tag( final String tagname) {

        this.tagname = tagname;
    }

}
