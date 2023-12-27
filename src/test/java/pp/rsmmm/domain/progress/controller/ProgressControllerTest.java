package pp.rsmmm.domain.progress.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pp.rsmmm.IntegrationTest;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.progress.dto.ProgressCreateRequestDto;
import pp.rsmmm.domain.progress.dto.ProgressNameModifyDto;
import pp.rsmmm.domain.progress.dto.ProgressOrderModifyDto;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.progress.repository.ProgressRepository;
import pp.rsmmm.domain.progress.service.ProgressService;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.team.repository.TeamRepository;
import pp.rsmmm.domain.teamsetting.entity.InviteStatus;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class ProgressControllerTest extends IntegrationTest {

    /*
    이 테스트 클래스는 스프링 서버 구동과 동시에 생성되는 dummy data를 대상으로 진행됨.
    테스트 메서드 내 dummy data활용은 주석으로 표시하였음.
     */

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ProgressRepository progressRepository;

    @Autowired
    TeamSettingRepository teamSettingRepository;

    @Autowired
    ProgressService progressService;

    private static HttpHeaders headers;

    private Member dummy_member01;
    private Member dummy_member04;
    private Member dummy_member07;

    @DisplayName("진행상황 생성 - 성공")
    @Test
    void createProgress_succeed() throws Exception {
        // given : dummy data 중, memberId=4번 사용자가 자신이 생성한 teamId=4번 팀 내에서 진행상황(Progress) 생성하기 위한 준비사항
        dummy_member04 = memberRepository.findByName("dummy_member04")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member04);
        ProgressCreateRequestDto progressCreateRequestDto = ProgressCreateRequestDto.of("Test_Progress");
        log.info("<progressCreateRequestDto>" + String.valueOf(progressCreateRequestDto));
        Long teamId = 4L; // dummy data로 생성된 teamId = 4번의 team을 대상으로 진행상황(Progress) 생성

        // when
        mvc.perform(post("/api/teams/" + teamId + "/progresses/create")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(progressCreateRequestDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @DisplayName("팀장이 진행상황 조회 - 성공")
    @Test
    void getProgressByTeamLeader_succeed() throws Exception {
        // given : dummy data 중, memberId=4번 사용자가 자신이 생성한 teamId=4번 팀 내에서 진행상황(Progress) 생성하기 위한 준비사항
        dummy_member04 = memberRepository.findByName("dummy_member04")
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없다"));
        getAccessToken(dummy_member04);
        Long teamId = 4L; // dummy data로 생성된 teamId = 4번의 team을 대상으로 진행상황(Progress) 생성
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("해당 팀을 찾을 수 없다"));
        Long progressId = createProgress(dummy_member04, team);

        // when
        mvc.perform(get("/api/teams/" + teamId + "/progresses/" + progressId)
                .headers(headers)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("팀원이 진행상황 조회 - 성공")
    @Test
    void getProgressByTeamMate_succeed() throws Exception {
        // given : dummy data 중, memberId=7번 사용자가 teamId=1번 팀으로부터 팀원으로 초대를 받은 상태
        dummy_member07 = memberRepository.findByName("dummy_member07")
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없다"));
        getAccessToken(dummy_member07);
        Long teamId = 1L; // dummy data로 생성된 teamId = 1번의 team에 이미 생성된 진행상황(Progress)을 대상으로 진행상황 조회할 것
        Long teamSettingId = 7L; // dummy data로 생성된 teamSettingId = 7번의 teamSetting이 바로 memberId=7번 사용자가 teamId=1번 팀으로부터 팀원으로 초대를 받은 상태임
        TeamSetting teamSetting = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 팀 구성을 찾을 수 없다"));
        Long memberId = dummy_member07.getId();
        boolean accept = true;
        respondToInvitation(teamId, memberId, accept, teamSettingId);
        log.info("[ 로그인 사용자 상태 : {} ]", teamSetting.getInviteStatus());
        Long progressId = 1L;

        // when
        mvc.perform(get("/api/teams/" + teamId + "/progresses/" + progressId)
                .headers(headers)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("팀장이 진행상황 삭제 - 성공")
    @Test
    void deleteProgressByTeamLeader_succeed() throws Exception {
        // given : dummy data 중, memberId=1번 사용자가 teamId=1번 팀, progressId=1번 진행상황을 생성
        dummy_member01 = memberRepository.findByName("dummy_member01")
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없다"));
        getAccessToken(dummy_member01);
        Long teamId = 1L;
        Long progressId = 1L;

        // when
        mvc.perform(delete("/api/teams/" + teamId + "/progresses/" + progressId)
                .headers(headers)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 해당 진행 상황을 찾아봄
                    Optional<Progress> deletedProgress = progressRepository.findById(progressId);

                    // 찾을 수 없다면 테스트 통과
                    assertFalse(deletedProgress.isPresent());
                });
    }

    @DisplayName("팀원이 진행상황 삭제 - 실패")
    @Test
    void modifyProgressOrder_fail() throws Exception {
        // given : dummy data 중, memberId=1번 사용자가 teamId=1번 팀, progressId=1번 진행상황을 생성
        // memberId=1번 사용자가 teamId=1번 팀으로부터 초대 받은 상태
        dummy_member07 = memberRepository.findByName("dummy_member07")
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없다"));
        getAccessToken(dummy_member07);
        Long teamId = 1L; // dummy data로 생성된 teamId = 1번의 team에 이미 생성된 진행상황(Progress)을 대상으로 진행상황 조회할 것
        Long teamSettingId = 7L; // dummy data로 생성된 teamSettingId = 7번의 teamSetting이 바로 memberId=7번 사용자가 teamId=1번 팀으로부터 팀원으로 초대를 받은 상태임
        TeamSetting teamSetting = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 팀 구성을 찾을 수 없다"));
        Long memberId = dummy_member07.getId();
        boolean accept = true;
        respondToInvitation(teamId, memberId, accept, teamSettingId);
        log.info("[ 로그인 사용자 상태 : {} ]", teamSetting.getInviteStatus());
        Long progressId = 1L;

        // when & then
        assertThrows(EntityNotFoundException.class, () -> progressService.deleteProgress(teamId, progressId));
    }

    @DisplayName("팀장이 진행상황 이름 변경 - 성공")
    @Test
    void modifyProgressNameByTeamLeader_succeed() throws Exception {
        // given : dummy data 중, memberId=1번 사용자가 teamId=1번 팀, progressId=1번 진행상황을 생성
        dummy_member01 = memberRepository.findByName("dummy_member01")
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없다"));
        getAccessToken(dummy_member01);
        Long teamId = 1L;
        Long progressId = 1L;

        ProgressNameModifyDto progressNameModifyDto = new ProgressNameModifyDto();
        progressNameModifyDto.setName("Name_changed_Progress");

        // when
        mvc.perform(put("/api/teams/" + teamId + "/progresses/" + progressId)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(progressNameModifyDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                            // 데이터베이스에서 수정된 진행상황을 찾음
                            Progress modifiedProgress = progressRepository.findById(progressId)
                                    .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));

                            // 이름이 바뀌었는지 확인
                            Assertions.assertEquals("Name_changed_Progress", modifiedProgress.getName());
                });
    }

    @DisplayName("팀원이 진행상황 이름 변경 - 실패")
    @Test
    void modifyProgressNameByTeamMate_fail() throws Exception {
        // given : dummy data 중, memberId=1번 사용자가 teamId=1번 팀, progressId=1번 진행상황을 생성
        dummy_member07 = memberRepository.findByName("dummy_member07")
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없다"));
        getAccessToken(dummy_member07);
        Long teamId = 1L; // dummy data로 생성된 teamId = 1번의 team에 이미 생성된 진행상황(Progress)을 대상으로 진행상황 조회할 것
        Long teamSettingId = 7L; // dummy data로 생성된 teamSettingId = 7번의 teamSetting이 바로 memberId=7번 사용자가 teamId=1번 팀으로부터 팀원으로 초대를 받은 상태임
        TeamSetting teamSetting = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 팀 구성을 찾을 수 없다"));
        Long memberId = dummy_member07.getId();
        boolean accept = true;
        respondToInvitation(teamId, memberId, accept, teamSettingId);
        log.info("[ 로그인 사용자 상태 : {} ]", teamSetting.getInviteStatus());
        Long progressId = 1L;

        ProgressNameModifyDto progressNameModifyDto = new ProgressNameModifyDto();
        progressNameModifyDto.setName("Name_changed_Progress");

        // when & then
        assertThrows(EntityNotFoundException.class, () -> progressService.modifyProgressName(progressNameModifyDto, teamId, progressId));
    }

    @DisplayName("팀장이 진행상황 순서 변경 - 성공")
    @Test
    void modifyProgressOrderByTeamLeader_succeed() throws Exception {
        // given : dummy data 중, memberId=1번 사용자가 teamId=1번 팀, progressId=1번 진행상황을 생성
        dummy_member01 = memberRepository.findByName("dummy_member01")
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없다"));
        getAccessToken(dummy_member01);
        Long teamId = 1L;

        Progress progress1 = progressRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));
        Progress progress2 = progressRepository.findById(2L)
                .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));

        ProgressOrderModifyDto progressOrderModifyDto = new ProgressOrderModifyDto();
        progressOrderModifyDto.setNumbering(2);

        // when
        mvc.perform(patch("/api/teams/" + teamId + "/progresses/" + progress1.getId())
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(progressOrderModifyDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 수정된 진행상황을 찾음
                    Progress modifiedProgress1 = progressRepository.findById(progress1.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));
                    Progress modifiedProgress2 = progressRepository.findById(progress2.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));


                    // 순서가 바뀌었는지 확인
                    assertEquals(2, modifiedProgress1.getNumbering());
                    assertEquals(1, modifiedProgress2.getNumbering());

                    log.info("[ progress1 - numbering : {} ]", modifiedProgress1.getNumbering());
                    log.info("[ progress2 - numbering : {} ]", modifiedProgress2.getNumbering());
                });
    }

    @DisplayName("팀원이 진행상황 순서 변경 - 성공")
    @Test
    void modifyProgressOrderByTeamMate_succeed() throws Exception {
        // given : dummy data 중, memberId=1번 사용자가 teamId=1번 팀, progressId=1번 진행상황을 생성
        dummy_member07 = memberRepository.findByName("dummy_member07")
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없다"));
        getAccessToken(dummy_member07);
        Long teamId = 1L; // dummy data로 생성된 teamId = 1번의 team에 이미 생성된 진행상황(Progress)을 대상으로 진행상황 조회할 것
        Long teamSettingId = 7L; // dummy data로 생성된 teamSettingId = 7번의 teamSetting이 바로 memberId=7번 사용자가 teamId=1번 팀으로부터 팀원으로 초대를 받은 상태임
        TeamSetting teamSetting = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 팀 구성을 찾을 수 없다"));
        Long memberId = dummy_member07.getId();
        boolean accept = true;
        respondToInvitation(teamId, memberId, accept, teamSettingId);
        log.info("[ 로그인 사용자 상태 : {} ]", teamSetting.getInviteStatus());

        Progress progress1 = progressRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));
        Progress progress2 = progressRepository.findById(2L)
                .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));

        ProgressOrderModifyDto progressOrderModifyDto = new ProgressOrderModifyDto();
        progressOrderModifyDto.setNumbering(2);

        // when
        mvc.perform(patch("/api/teams/" + teamId + "/progresses/" + progress1.getId())
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(progressOrderModifyDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 수정된 진행상황을 찾음
                    Progress modifiedProgress1 = progressRepository.findById(progress1.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));
                    Progress modifiedProgress2 = progressRepository.findById(progress2.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 진행상황을 찾을 수 없다"));


                    // 순서가 바뀌었는지 확인
                    assertEquals(2, modifiedProgress1.getNumbering());
                    assertEquals(1, modifiedProgress2.getNumbering());

                    log.info("[ progress1 - numbering : {} ]", modifiedProgress1.getNumbering());
                    log.info("[ progress2 - numbering : {} ]", modifiedProgress2.getNumbering());
                });
    }

    private String getAccessToken(Member member) throws Exception {
        log.info("[member : {}", member.getName());
        String tokenType = "access";
        String accessToken = tokenProvider.issueToken(member, tokenType);

        // 토큰이 정상적으로 생성되었는지 확인
        assertNotNull(accessToken);

        // 토큰이 요청 헤더에 정상적으로 로드되는지 확인
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);  // Bearer는 토큰 타입이며, 필요에 따라 다르게 설정할 수 있다.
        log.info("[Headers] ; {}", String.valueOf(headers));

        return String.valueOf(accessToken);
    }

    private Long createProgress(Member teamLeader, Team team) {

        Progress progress = Progress.builder()
                .name("Test_Progress")
                .numbering(1)
                .team(team)
                .ticketList(new ArrayList<>())
                .build();

        progressRepository.save(progress);
        Long progressId = progress.getId();
        return progressId;
    }

    public void respondToInvitation(Long teamId, Long inviteeId, boolean accept, Long teamSettingId) {
        // 팀 및 팀구성이 존재하는지 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않거나 찾을 수 없습니다."));

        TeamSetting teamSetting = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("팀 구성이 존재하지 않거나 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 초대받은 사용자인지 확인
        Member member = memberRepository.findById(inviteeId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (member.getId() != inviteeId) {
            throw new EntityNotFoundException(team.getName() +"팀의 초대에 수락 또는 거절할 권한이 없습니다.");
        }

        if (accept) {
            teamSetting.setInviteStatus(InviteStatus.ACCEPTED);
        } else {
            teamSetting.setInviteStatus(InviteStatus.REFUSED);
        }
    }
}