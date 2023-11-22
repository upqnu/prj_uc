package pp.rsmmm.domain.progress.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.ticket.entity.Ticket;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Progress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer numbering;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Progress(String name, Integer numbering, Team team) {
        this.name = name;
        this.numbering = numbering;
        this.team = team;
    }

    public void progressNameUpdate(String name) {
        this.name = name;
    }

    public void progressOrderUpdate(Integer numbering) {
        this.numbering = numbering;
    }

}
