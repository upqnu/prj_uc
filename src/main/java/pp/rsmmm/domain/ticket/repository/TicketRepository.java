package pp.rsmmm.domain.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.ticket.entity.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    int countByProgress(Progress progress);

    List<Ticket> findTicketByProgress(Progress progress);
}
