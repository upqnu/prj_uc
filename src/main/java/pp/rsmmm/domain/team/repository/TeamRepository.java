package pp.rsmmm.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pp.rsmmm.domain.team.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    public boolean existsByName(String teamName);
}
