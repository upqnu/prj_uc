package pp.rsmmm.domain.teamsetting.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.team.repository.TeamRepository;
import pp.rsmmm.domain.teamsetting.entity.InviteStatus;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@Service
public class TeamSettingService {

    private final TeamSettingRepository teamSettingRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public void createTeamSetting(Member member, Team team) {
        TeamSetting teamSetting = TeamSetting.builder()
                .member(member)
                .team(team)
                .inviteStatus(InviteStatus.INVITING)
                .build();

        teamSettingRepository.save(teamSetting);
    }

    @Transactional
    public TeamSetting getTeam(Long teamId) {

        // 해당 team이 존재하는지 확인
        TeamSetting teamSetting = teamSettingRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        // 해당 team의 팀장 또는 팀원만 팀구성(teamSetting) 확인이 가능
        String memberName = tokenProvider.getMemberNameFromToken();

        if (!teamSetting.getMember().getName().equals(memberName)) {
            throw new EntityNotFoundException("팀을 조회할 권한이 없습니다.");
        }

        return teamSetting;
    }

    @Transactional
    public void sendInvitation(Long teamId, String invitedMemberName) {
        // team 찾기
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 팀 리더인지 확인
        String memberName = tokenProvider.getMemberNameFromToken();
        TeamSetting teamSetting = teamSettingRepository.findByMember_Name(memberName)
                .orElseThrow(() -> new EntityNotFoundException("당신이 만든 팀이 존재하지 않습니다."));

        if (teamSetting.getInviteStatus() != InviteStatus.INVITING) {
            throw new IllegalStateException("팀원을 초대할 권한이 없습니다.");
        }

        // 초대장을 보냄
        Member invitedMember = memberRepository.findByName(invitedMemberName)
                .orElseThrow(() -> new EntityNotFoundException("초대할 사용자를 찾을 수 없습니다."));

        TeamSetting addedTeamSetting = TeamSetting.builder()
                .team(team)
                .member(invitedMember)
                .inviteStatus(InviteStatus.RECEIVED)
                .build();

        teamSettingRepository.save(addedTeamSetting);
    }

}
