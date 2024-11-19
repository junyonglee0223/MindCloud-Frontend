package kr.brain.our_app.user.dto;

import kr.brain.our_app.user.domain.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
    private String id;
    private String email;
    private String userName;


    public User toEntity() {
        return User.builder()
                .id(this.id)
                .userName(this.userName)
                .email(this.email)
                .build();
    }
}


