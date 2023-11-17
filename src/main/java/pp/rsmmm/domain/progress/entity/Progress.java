package pp.rsmmm.domain.progress.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.ticket.entity.Ticket;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Entity
@NoArgsConstructor
public class Progress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer numbering;

    @OneToMany(mappedBy = "progress")
    private List<Ticket> ticketList;

    @ManyToOne
    @JoinColumn(name = "team_setting_id")
    private TeamSetting teamSetting;
}
