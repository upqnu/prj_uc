package pp.rsmmm.domain.ticket.entity;

import jakarta.persistence.*;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.global.config.model.BaseEntity;

import java.time.LocalDateTime;

@Entity
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer order;

    @Column(nullable = false)
    private String tag;

    private Double personHour;

    private LocalDateTime dueDate;

    @ManyToOne
    private Progress progress;

    @ManyToOne
    private TeamSetting teamSetting;

}
