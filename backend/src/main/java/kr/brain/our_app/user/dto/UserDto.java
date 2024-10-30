package kr.brain.our_app.user.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
    @NotEmpty
    private String id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String userName;
}
