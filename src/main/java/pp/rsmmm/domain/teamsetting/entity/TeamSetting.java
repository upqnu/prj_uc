package pp.rsmmm.domain.teamsetting.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Entity
@NoArgsConstructor
public class TeamSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_setting_id")
    private Long id;

    private boolean isLeader;

    private boolean isMate;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
