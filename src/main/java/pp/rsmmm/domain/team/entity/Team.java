package pp.rsmmm.domain.team.entity;

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
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String kanban;

    @Builder
    public Team(String name, String kanban) {
        this.name = name;
        this.kanban = kanban;
    }

    @OneToMany(mappedBy = "team")
    private List<TeamSetting> teamSettingList;

}
