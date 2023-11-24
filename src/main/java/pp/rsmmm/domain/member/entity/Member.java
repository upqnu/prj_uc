package pp.rsmmm.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String name, String password, String email, Authority authority) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.authority = Authority.ROLE_MEMBER;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "member")
    private List<TeamSetting> teamSettingList;

}
