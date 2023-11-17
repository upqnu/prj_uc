package pp.rsmmm.domain.team.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Entity
@NoArgsConstructor
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false, unique = false)
    private String name;

    @Column(nullable = false)
    private String kanban;

    @OneToMany(mappedBy = "team")
    private List<TeamSetting> teamSettingList;
}
