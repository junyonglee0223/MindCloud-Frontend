package kr.brain.our_app.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", schema = "PUBLIC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    @Column(name = "username")
    private String userName;

    @Column(name = "gmail")
    private String email;
}


