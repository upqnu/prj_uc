package pp.rsmmm.domain.progress.entity;

import jakarta.persistence.*;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.ticket.entity.Ticket;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Entity
public class Progress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer order;

    @OneToMany
    private List<Ticket> ticketList;

    @ManyToOne
    private TeamSetting teamSetting;
}
