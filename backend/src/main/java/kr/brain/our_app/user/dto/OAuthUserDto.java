package kr.brain.our_app.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OAuthUserDto {
    @NotEmpty
    private String authId;

    @Email
    private String email;

    @NotEmpty
    private String userName;
}
