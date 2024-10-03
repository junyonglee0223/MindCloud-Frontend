package kr.brain.our_app.tag.dto;

import jakarta.persistence.*;
import kr.brain.our_app.bookmark.dto.Bookmark;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="tag")
@Getter
@Setter
public class Tag {
    @Id
    private Long id;//user의 system id
    private String tagname;

    @ManyToOne
    @JoinColumn(name = "bookmarkid")
    private Bookmark bookmark;

}

