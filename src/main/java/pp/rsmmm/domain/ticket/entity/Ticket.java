package pp.rsmmm.domain.ticket.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.global.config.model.BaseEntity;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer numbering;

    @Column(nullable = false)
    private String tag;

    private Double personHour;

    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "progress_id")
    private Progress progress;

    @ManyToOne
    @JoinColumn(name = "team_setting_id")
    private TeamSetting teamSetting;

}
