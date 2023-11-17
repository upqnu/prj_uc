package pp.rsmmm.domain.jwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    private String memberName;

    public RefreshToken(String refreshToken, String memberName) {
        this.refreshToken = refreshToken;
        this.memberName = memberName;
    }
}
