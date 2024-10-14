package kr.brain.our_app.bookmark.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

// client에서 보내는 북마크 생성 요청 데이터를 담는 역할을 한다.
// client에서 url을 담아서, 북마크 생성해 주세요 ~ 하는것
// 태글과 다르게, 나는 title 까지 같이 request 보내는것으로 했따.
// 이후에 gpt를 이용해서 title 추천까지 해줄거면, title 제거하고 로직 수정하면 됨

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter

public class BookmarkCreateRequest {
    @NotEmpty
    private String title;
    private String url;
    private Set<String> tags;
}
