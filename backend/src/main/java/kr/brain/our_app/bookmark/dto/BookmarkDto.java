package kr.brain.our_app.bookmark.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
    private String bookmarkName;
    private String url;
}
