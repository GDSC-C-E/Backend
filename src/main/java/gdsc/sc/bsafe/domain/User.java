package gdsc.sc.bsafe.domain;

import gdsc.sc.bsafe.global.common.BaseTimeEntity;
import gdsc.sc.bsafe.global.auth.Password;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static gdsc.sc.bsafe.domain.Authority.ROLE_USER;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String id;

    @Embedded
    private Password password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Authority authority = ROLE_USER;

    public User(String id, Password password, String email, String name) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.authority = ROLE_USER;
    }
}