package kr.brain.our_app.user.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserIdDto {
    @NotEmpty
    private String id;
}
