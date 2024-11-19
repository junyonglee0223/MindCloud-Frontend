package kr.brain.our_app.tag.dto;

import kr.brain.our_app.tag.domain.Tag;
import kr.brain.our_app.user.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private String id;
    private String tagName;

    public Tag toEntity() {
        return Tag.builder()
                .id(this.id)
                .tagName(this.tagName)
                .build();
    }

}
