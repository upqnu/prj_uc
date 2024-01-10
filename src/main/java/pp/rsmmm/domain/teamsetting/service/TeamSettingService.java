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

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamSettingService {

    private final TeamSettingRepository teamSettingRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    /**
     * 팀구성(TeamSetting) 생성 로직(특정 사용자가 Team 생성 시, 자동적으로 해당 팀의 팀장이 되도록 동시에 이 메서드가 실행됨)
     * @param member
     * @param team
     */
    @Transactional
    public void createTeamSetting(Member member, Team team) {
        TeamSetting teamSetting = TeamSetting.builder()
                .member(member)
                .team(team)
                .inviteStatus(InviteStatus.INVITING)
                .build();

        teamSettingRepository.save(teamSetting);
    }

    /**
     * 팀원으로 초대 (팀장만 가능)
     * @param teamId
     * @param invitedMemberName
     */
    @Transactional
    public void sendInvitation(Long teamId, String invitedMemberName) {
        // team 찾기
        Team team = findTeam(teamId);

        // 현재 로그인한 사용자가 팀 리더인지 확인
        String memberName = tokenProvider.getMemberNameFromToken();
        List<TeamSetting> teamSettings = null;
        try {
            teamSettings = teamSettingRepository.findByMember_Name(memberName);
        } catch (EntityNotFoundException e) {
            System.out.println("당신이 속한 팀이 존재하지 않습니다.");
        }

        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getTeam() != team) {
                continue;
            }

            if (teamSetting.getInviteStatus() != InviteStatus.INVITING) {
                throw new IllegalStateException("팀원을 초대할 권한이 없습니다.");
            }
        }

        // 초대장을 보냄 (초대를 받는 사용자의 상태가 RECEIVED가 됨)
        Member invitedMember = memberRepository.findByName(invitedMemberName)
                .orElseThrow(() -> new EntityNotFoundException("초대할 사용자를 찾을 수 없습니다."));

        TeamSetting addedTeamSetting = TeamSetting.builder()
                .team(team)
                .member(invitedMember)
                .inviteStatus(InviteStatus.RECEIVED)
                .build();

        teamSettingRepository.save(addedTeamSetting);
    }

    /**
     * 팀원으로의 초대에 승낙 또는 거절
     * @param teamId
     * @param inviteeId
     * @param accept
     */
    @Transactional
    public void respondToInvitation(Long teamId, Long inviteeId, boolean accept) {
        // 팀 및 팀구성이 존재하는지 확인
        Team team = findTeam(teamId);

        List<TeamSetting> teamSettings = null;
        try {
            teamSettings = teamSettingRepository.findByTeam(team);
        } catch (EntityNotFoundException e) {
            System.out.println("팀을 찾을 수 없습니다.");
        }

        // 현재 로그인한 사용자가 초대받은 사용자인지 확인
        String memberName = tokenProvider.getMemberNameFromToken();
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (member.getId() != inviteeId) {
            throw new EntityNotFoundException(team.getName() +"팀의 초대에 수락 또는 거절할 권한이 없습니다.");
        }

        // 초대받은 사용자의 InviteStatus가 RECEIVED인 경우에만 초대를 수락 또는 거절
        TeamSetting theTeamSetting = null;
        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getMember() != member) {
                continue;
            }

            if (teamSetting.getInviteStatus() != InviteStatus.RECEIVED) {
                throw new EntityNotFoundException(team.getName() +"팀의 초대에 수락 또는 거절할 권한이 없습니다.");
            }

            theTeamSetting = teamSetting;
        }

        if (accept) {
            theTeamSetting.setInviteStatus(InviteStatus.ACCEPTED);
        } else {
            theTeamSetting.setInviteStatus(InviteStatus.REFUSED);
        }

    }

    /**
     * 팀 존재여부 확인
     * @param teamId
     * @return
     */
    private Team findTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않거나 찾을 수 없습니다."));

        return team;
    }

}
