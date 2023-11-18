package pp.rsmmm.domain.teamsetting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;

@Repository
public interface TeamSettingRepository extends JpaRepository<TeamSetting, Long> {
}
