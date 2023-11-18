package pp.rsmmm.domain.teamsetting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;

@RequiredArgsConstructor
@Service
public class TeamSettingService {

    private final TeamSettingRepository teamSettingRepository;

    public void createTeamSetting(Member member, Team team) {
        TeamSetting teamSetting = TeamSetting.builder()
                .isLeader(true)
                .isMate(false)
                .team(team)
                .member(member)
                .build();

        teamSettingRepository.save(teamSetting);
    }

}
