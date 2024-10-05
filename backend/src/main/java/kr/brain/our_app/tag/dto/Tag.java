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
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

}

//@Entity
//@Table(name ="tag", uniqueConstraints = @UniqueConstraint(columnNames = {"tagname"}))
//@Getter
//@Setter
//public class Tag {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true, nullable = false)
//    private String tagname;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "bookmark_id")
//    private Bookmark bookmark;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;
//}