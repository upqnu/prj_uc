package pp.rsmmm.domain.team.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import pp.rsmmm.IntegrationTest;
import pp.rsmmm.domain.member.dto.SignUpRequestDto;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.team.dto.TeamCreateRequestDto;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.team.repository.TeamRepository;
import pp.rsmmm.domain.teamsetting.entity.InviteStatus;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;
import pp.rsmmm.domain.teamsetting.service.TeamSettingService;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pp.rsmmm.domain.member.entity.Authority.ROLE_MEMBER;

@Slf4j
class TeamControllerTest extends IntegrationTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamSettingRepository teamSettingRepository;

    @Autowired
    TeamSettingService teamSettingService;

    private static HttpHeaders headers;

    private Member teamLeader;
    private Member teamMate;
    private Member notTeamMember;

    @DisplayName("팀 생성 - 성공")
    @Test
    public void createTeam_succeed() throws Exception {
        // given
        membersSetUp();
        getAccessToken(teamLeader);
        TeamCreateRequestDto teamCreateRequestDto = new TeamCreateRequestDto("testTeam", "testKanban");
        System.out.println("<teamCreateRequestDto>" + String.valueOf(teamCreateRequestDto));

        // when
        mvc.perform(post("/api/teams/create")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamCreateRequestDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists());

//        Team team = teamRepository.findById(2L)
//                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않는다.")); // test에서 생성된 팀은 1개여서 teamId = 1L 이다. 실제 1L이라는 id를 가진 팀의 존재여부를 확인.
//
//        // teamSetting 확인
//        TeamSetting teamSetting = teamSettingRepository.findById(4L)
//                .orElseThrow(() -> new EntityNotFoundException("팀 설정이 없다."));
//
//        System.out.println("팀 이름 :" + team.getName());
//        System.out.println("팀 세팅 id :" + teamSetting.getId());
    }

    @DisplayName("팀원 초대1 - 성공")
    @Test
    public void inviteMate1_succeed() throws Exception {
        // given
        membersSetUp();
        getAccessToken(teamLeader);
        ArrayList<Long> ids = createTeam(teamLeader);
        Long teamId = ids.get(0);

        String invitedMemberName = teamMate.getName();
        System.out.println("<초대할 사용자 이름> : " + invitedMemberName);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않음"));
        log.info("[team id : {}]", teamId);

        // when
        mvc.perform(post("/api/teams/" + teamId + "/invite")
                .headers(headers)
                .param("invitedMemberName", invitedMemberName)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());

        log.info("[invitee id : {}]", teamMate.getId());

//        // teamSetting 확인
//        TeamSetting teamSetting = teamSettingRepository.findById(2L)
//                .orElseThrow(() -> new EntityNotFoundException("팀 설정이 없다."));
//
//        System.out.println("팀 세팅 id :" + teamSetting.getId());
    }

    @DisplayName("팀원 초대2 - 성공")
    @Test
    public void inviteMate2_succeed() throws Exception {
        // given
        membersSetUp();
        getAccessToken(teamLeader);
        ArrayList<Long> ids = createTeam(teamLeader);
        Long teamId = ids.get(0);

        String invitedMemberName = notTeamMember.getName();
        System.out.println("<초대할 사용자 이름> : " + invitedMemberName);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않음"));
        log.info("[team id : {}]", teamId);

        // when
        mvc.perform(post("/api/teams/" + teamId + "/invite")
                        .headers(headers)
                        .param("invitedMemberName", invitedMemberName)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());

        log.info("[invitee id : {}]", notTeamMember.getId());
    }

    @DisplayName("팀원 초대에 수락 - 성공")
    @Test
    public void acceptInvitation_succeed() throws Exception {
        // given
        membersSetUp();
        getAccessToken(teamLeader);
        ArrayList<Long> ids = createTeam(teamLeader);
        Long teamId = ids.get(0);
        Long teamSettingId = ids.get(1);

        TeamSetting teamSetting = sendInvitation(teamId, teamSettingId, teamMate.getName());
        getAccessToken(teamMate);

//        Long teamId = 1L;
//        Team team = teamRepository.findById(1L)
//                .orElseThrow(() -> new EntityNotFoundException("팀이 읎다...")); // test에서 생성된 팀은 1개여서 teamId = 1L 이다. 실제 1L이라는 id를 가진 팀의 존재여부를 확인.
//        System.out.println("<team> : " + String.valueOf(team));
//
//        Long inviteeId = teamMate.getId();
//        Member member = memberRepository.findById(inviteeId)
//                .orElseThrow(() -> new EntityNotFoundException("멤버가 없다"));
//        System.out.println("<member> : " + String.valueOf(member));

        boolean accept = true;

        // when
        mvc.perform(post("/api/teams/" + teamId + "/invitation/" + teamMate.getId())
                .headers(headers)
                .param("accept", String.valueOf(accept))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("팀원 초대에 거절 - 성공")
    @Test
    public void refuseInvitation_succeed() throws Exception {
        // given
        membersSetUp();
        getAccessToken(teamLeader);
        ArrayList<Long> ids = createTeam(teamLeader);
        Long teamId = ids.get(0);
        Long teamSettingId = ids.get(1);

        TeamSetting teamSetting = sendInvitation(teamId, teamSettingId, notTeamMember.getName());
        getAccessToken(notTeamMember);

        boolean accept = false;

        // when
        mvc.perform(post("/api/teams/" + teamId + "/invitation/" + notTeamMember.getId())
                        .headers(headers)
                        .param("accept", String.valueOf(accept))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("팀장이 팀 조회 - 성공")
    @Test
    public void getTeamByTeamLeader_succeed() throws Exception {
        // given
        membersSetUp();
        getAccessToken(teamLeader);
        ArrayList<Long> ids = createTeam(teamLeader);
        Long teamId = ids.get(0);
        Long teamSettingId = ids.get(1);
//        TeamSetting teamSetting = teamSettingRepository.findById(teamSettingId)
//                        .orElseThrow(() -> new EntityNotFoundException("~~"));
//        String inviteStatus = String.valueOf(teamSetting.getInviteStatus());
//        log.info("[team id : {}] , [teamSetting id : {}]", teamId, teamSettingId);

        // when
        mvc.perform(get("/api/teams/" + teamId)
                .headers(headers)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//                .andExpect(jsonPath("$.teamId").value(teamId))
//                .andExpect(jsonPath("$.inviteStatus").value(inviteStatus));
    }

    void membersSetUp() throws Exception {
        teamLeader = createMember("teamLeader");
        teamMate = createMember("teamMate");
        notTeamMember = createMember("notTeamMember");

        log.info("teamLeader id : {} / teamMate id : {} / notTeamMember : {}", teamLeader.getId(), teamMate.getId(), notTeamMember.getId());
    }

    private Member createMember(String memberPosition) {
        if (memberPosition.equals("teamLeader")) {
            teamLeader = Member.builder()
                    .name("teamLeader")
                    .password(passwordEncoder.encode("zxcv1234"))
                    .email("teamLeader@email.com")
                    .authority(ROLE_MEMBER)
                    .build();

            memberRepository.save(teamLeader);

            return teamLeader;
        }

        if (memberPosition.equals("teamMate")) {
            teamMate = Member.builder()
                    .name("teamMate")
                    .password(passwordEncoder.encode("zxcv1234"))
                    .email("teamMate@email.com")
                    .authority(ROLE_MEMBER)
                    .build();

            memberRepository.save(teamMate);

            return teamMate;
        }

        if (memberPosition.equals("notTeamMember")) {
            notTeamMember = Member.builder()
                    .name("notTeamMember")
                    .password(passwordEncoder.encode("zxcv1234"))
                    .email("notTeamMember@email.com")
                    .authority(ROLE_MEMBER)
                    .build();

            memberRepository.save(notTeamMember);

            return notTeamMember;
        }

        return null;
    }

    private void getAccessToken(Member member) throws Exception {
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
    }

    private ArrayList<Long> createTeam(Member teamLeader) {
        Team team = Team.builder()
                .name("테스트-팀1")
                .kanban("테스트-칸반1")
                .build();

        teamRepository.save(team);

        TeamSetting teamSetting = TeamSetting.builder()
                .member(teamLeader)
                .team(team)
                .inviteStatus(InviteStatus.INVITING)
                .build();

        teamSettingRepository.save(teamSetting);

        ArrayList<Long> ids = new ArrayList<>();
        ids.add(team.getId());
        ids.add(teamSetting.getId());

        return ids;
    }

    private TeamSetting sendInvitation(Long teamId, Long teamSettingId, String inviteeName) {
        // team 찾기
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않거나 찾을 수 없습니다."));

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
        Member invitedMember = memberRepository.findByName(inviteeName)
                .orElseThrow(() -> new EntityNotFoundException("초대할 사용자를 찾을 수 없습니다."));

        TeamSetting addedTeamSetting = TeamSetting.builder()
                .team(team)
                .member(invitedMember)
                .inviteStatus(InviteStatus.RECEIVED)
                .build();

        teamSettingRepository.save(addedTeamSetting);

        return addedTeamSetting;
    }

}
