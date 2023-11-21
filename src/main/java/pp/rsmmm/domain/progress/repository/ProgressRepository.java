package pp.rsmmm.domain.progress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.team.entity.Team;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

    int countByTeam(Team team);
}
