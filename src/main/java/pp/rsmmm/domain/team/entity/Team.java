package pp.rsmmm.domain.team.entity;

import jakarta.persistence.*;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Entity
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, unique = false)
    private String name;

    @Column(nullable = false)
    private String kanban;

    @OneToMany
    private List<TeamSetting> teamSettingList;
}
