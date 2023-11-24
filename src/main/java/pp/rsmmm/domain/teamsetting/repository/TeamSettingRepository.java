package pp.rsmmm.domain.teamsetting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamSettingRepository extends JpaRepository<TeamSetting, Long> {

    List<TeamSetting> findByMember_Name(String name);

    List<TeamSetting> findByMember(Member member);

    List<TeamSetting> findByTeam(Team team);
}
