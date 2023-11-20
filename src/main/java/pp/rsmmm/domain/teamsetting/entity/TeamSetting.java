package pp.rsmmm.domain.teamsetting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@ToString(exclude = "member")
@Getter
@Entity
@NoArgsConstructor
public class TeamSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_setting_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private Team team;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

    @Builder
    public TeamSetting(Team team, Member member, InviteStatus inviteStatus) {
        this.team = team;
        this.member = member;
        this.inviteStatus = inviteStatus;
    }

}
