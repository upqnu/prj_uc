package pp.rsmmm.domain.progress.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.progress.dto.ProgressCreateRequestDto;
import pp.rsmmm.domain.progress.dto.ProgressCreateResponseDto;
import pp.rsmmm.domain.progress.dto.ProgressNameModifyDto;
import pp.rsmmm.domain.progress.dto.ProgressOrderModifyDto;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.progress.repository.ProgressRepository;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.team.repository.TeamRepository;
import pp.rsmmm.domain.teamsetting.entity.InviteStatus;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final TeamSettingRepository teamSettingRepository;
    private final TeamRepository teamRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    /**
     * 진행상황(Progress) 생성 로직
     * @param teamId
     * @param progressCreateRequestDto
     * @return
     */
    @Transactional
    public ProgressCreateResponseDto createProgress(Long teamId, ProgressCreateRequestDto progressCreateRequestDto) {
        // 현재 로그인한 유저가 해당 팀의 팀장인지 여부 확인
        isTeamLeader(teamId);

        // 해당 팀이 존재하는지 여부 확인
        Team team = existsTeam(teamId);

        // Progress 생성
        int numOfProgress = progressRepository.countByTeam(team) + 1; // 이미 생성된 Progress의 숫자 + 1 이 numbering 값이 되어야 함.

        Progress progress = Progress.builder()
                .name(progressCreateRequestDto.getName())
                .numbering(numOfProgress)
                .team(team)
                .ticketList(new ArrayList<>())
                .build();

        progressRepository.save(progress);

        // Progress 생성 통보
        return ProgressCreateResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message("<" + progress.getName() + "> 진행상황이 생성되었습니다.")
                .build();
    }

    /**
     * 진행상황(progress) 조회 로직
     * @param teamId
     * @param progressId
     * @return
     */
    @Transactional
    public Progress getProgress(Long teamId, Long progressId) {
        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원인지 확인
        isTeamLeaderOrMate(teamId);

        // 해당 진행상황이 존재하는지 여부 확인
        Progress progress = existsProgress(progressId);

        return progress;
    }

    /**
     * 진행상황(progress) 삭제 로직
     * @param teamId
     * @param progressId
     */
    public void deleteProgress(Long teamId, Long progressId) {
        // 해당 팀, 진행상황이 존재하는지 확인
        Team team = existsTeam(teamId);
        Progress progress = existsProgress(progressId);

        // 현재 로그인한 유저가 해당 팀의 팀장인지 여부 확인
        isTeamLeader(teamId);

        // 진행상황 삭제
        progressRepository.deleteById(progressId);
    }

    /**
     * 진행상황(progress) 이름 변경 로직
     * @param progressNameModifyDto
     * @param teamId
     * @param progressId
     * @return
     */
    public Progress modifyProgressName(ProgressNameModifyDto progressNameModifyDto, Long teamId, Long progressId) {
        // 해당 팀, 진행상황이 존재하는지 확인
        Team team = existsTeam(teamId);
        Progress progress = existsProgress(progressId);

        // 현재 로그인한 유저가 해당 팀의 팀장인지 여부 확인
        isTeamLeader(teamId);

        // 진행상황 이름 변경
        progress.progressNameUpdate(progressNameModifyDto.getName());
        progressRepository.save(progress);
        return progress;
    }

    /**
     * 진행상황(progress) 순서 변경 로직
     * @param progressOrderModifyDto
     * @param teamId
     * @param progressId
     * @return
     */
    public Progress modifyProgressOrder(ProgressOrderModifyDto progressOrderModifyDto, Long teamId, Long progressId) {
        // 해당 팀, 진행상황이 존재하는지 확인
        Team team = existsTeam(teamId);
        Progress progress = existsProgress(progressId);

        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원인지 확인
        isTeamLeaderOrMate(teamId);

        // 진행상황 순서 변경 (모든 진행상황의 순서가 변경되어야 함.)
        int initNum = progress.getNumbering(); // 원래 번호
        int newNum = progressOrderModifyDto.getNumbering(); // 바꾸려고 입력된 번호

        List<Progress> progressList = progressRepository.findProgressByTeam(team);
        for (Progress p : progressList) {
            int otherNum = p.getNumbering();
            // 바꾸려는 진행상황의 순서 변경
            if (p == progress) {
                progress.progressOrderUpdate(newNum);
                progressRepository.save(p);
            }
            // 같은 팀 내 다른 진행상황의 순서 변경
            else {
                if (initNum < newNum && otherNum > initNum && otherNum <= newNum) {
                    p.progressOrderUpdate(otherNum - 1);
                    progressRepository.save(p);
                }
                else if (initNum > newNum && otherNum < initNum && otherNum >= newNum) {
                    p.progressOrderUpdate(otherNum + 1);
                    progressRepository.save(p);
                }
            }
        }

        return progress;
    }

    /**
     * 현재 로그인한 사용자가 팀장일 경우, team을 반환
     * @param teamId
     * @return
     */
    private void isTeamLeader(Long teamId) {
        String leaderName = tokenProvider.getMemberNameFromToken();
        Member leaderMember = memberRepository.findByName(leaderName)
                .orElseThrow(() -> new EntityNotFoundException("팀장 여부를 확인할 수 없습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        List<TeamSetting> teamSettings = teamSettingRepository.findByTeam(team);
        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getInviteStatus() != InviteStatus.INVITING) {
                continue;
            }

            if (teamSetting.getMember() != leaderMember) {
                throw new EntityNotFoundException("팀장만이 이 기능의 대한 권한이 있습니다.");
            }
        }
    }

    /**
     * 로그인한 사용자가 해당 팀의 팀장 또는 팀원인지 확인
     * @param teamId
     */
    private void isTeamLeaderOrMate(Long teamId) {
        String memberName = tokenProvider.getMemberNameFromToken();
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException("팀장 또는 팀원을 찾을 수 없습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        List<TeamSetting> teamSettings = teamSettingRepository.findByTeam(team);
        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getMember() != member) {
                continue;
            }

            if (teamSetting.getInviteStatus() == InviteStatus.RECEIVED || teamSetting.getInviteStatus() == InviteStatus.REFUSED) {
                throw new IllegalArgumentException("진행상황을 확인할 수 있는 권한이 없습니다.");
            }
        }
    }

    /**
     * 팀 존재 여부 (존재하면 해당 팀 반환)
     * @param teamId
     * @return
     */
    private Team existsTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않거나 찾을 수 없습니다."));

        return team;
    }

    /**
     * 진행상황(progress) 존재 여부 (존재하면 해당 진행상황 반환)
     * @param progressId
     * @return
     */
    private Progress existsProgress(Long progressId) {
        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new EntityNotFoundException("진행상황이 존재하지 않거나 찾을 수 없습니다."));

        return progress;
    }
}
