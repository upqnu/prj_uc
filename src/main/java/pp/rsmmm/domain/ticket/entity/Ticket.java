package pp.rsmmm.domain.ticket.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.ticket.service.TicketService;
import pp.rsmmm.global.config.model.BaseEntity;

import java.time.LocalDateTime;

@Getter
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

    private Long memberId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "progress_id")
    private Progress progress;

    @Builder
    public Ticket(String title, Integer numbering, String tag, Double personHour, LocalDateTime dueDate, Progress progress, Long memberId) {
        this.title = title;
        this.numbering = numbering;
        this.tag = tag;
        this.personHour = personHour;
        this.dueDate = dueDate;
        this.progress = progress;
        this.memberId = memberId;
    }

    public void modifyTicket(String title, String tag, Double personHour, LocalDateTime dueDate, Long memberId) {
        this.title = title;
        this.tag = tag;
        this.personHour = personHour;
        this.dueDate = dueDate;
        this.memberId = memberId;
    }

}
