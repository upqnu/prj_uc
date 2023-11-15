package pp.rsmmm.domain.member.entity;

import jakarta.persistence.*;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @OneToMany
    private List<TeamSetting> teamSettingList;

    @Enumerated(EnumType.STRING)
    private Authority authority;

}
