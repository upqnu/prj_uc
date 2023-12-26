package pp.rsmmm.domain.team.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import pp.rsmmm.IntegrationTest;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.team.dto.TeamCreateRequestDto;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.team.repository.TeamRepository;
import pp.rsmmm.domain.team.service.TeamService;
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

    @Autowired
    TeamService teamService;

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
        log.info("<teamCreateRequestDto>" + String.valueOf(teamCreateRequestDto));

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
        String accessToken = getAccessToken(teamLeader);
        log.info("[ token : {} ]", accessToken);
        ArrayList<Long> ids = createTeam(teamLeader);
        Long teamId = ids.get(0);
        Long teamSettingId = ids.get(1);
        TeamSetting teamSetting1 = teamSettingRepository.findById(teamSettingId)
                        .orElseThrow(() -> new EntityNotFoundException("팀 구성이 존재하지 않음"));
        log.info("[ teamSetting Id : {} / teamLeader의 상태 : {} ]", teamSettingId, teamSetting1.getInviteStatus());
        TeamSetting teamSetting2 = sendInvitation(teamId, teamSettingId, teamMate.getName());
        log.info("[ teamSetting Id : {} / teamMate의 상태 : {} ]", teamSetting2.getId(), teamSetting2.getInviteStatus());
        getAccessToken(teamMate);
        boolean accept = true;

        // when
        mvc.perform(post("/api/teams/" + teamId + "/invitation/" + teamMate.getId())
                .headers(headers)
                .param("accept", String.valueOf(accept))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());

        log.info("[ teamSetting Id : {} / teamMate의 상태 : {} ]", teamSetting2.getId(), teamSetting2.getInviteStatus());
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
        TeamSetting teamSetting1 = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("팀 구성이 존재하지 않음"));
        log.info("[ teamSetting Id : {} / teamLeader의 상태 : {} ]", teamSettingId, teamSetting1.getInviteStatus());
        TeamSetting teamSetting2 = sendInvitation(teamId, teamSettingId, notTeamMember.getName());
        log.info("[ teamSetting Id : {} / teamMate의 상태 : {} ]", teamSetting2.getId(), teamSetting2.getInviteStatus());
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

        log.info("[ teamSetting Id : {} / teamMate의 상태 : {} ]", teamSetting2.getId(), teamSetting2.getInviteStatus());
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
        TeamSetting teamSetting1 = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("팀 구성이 존재하지 않음"));
        log.info("[ teamSetting Id : {} / teamLeader의 상태 : {} ]", teamSettingId, teamSetting1.getInviteStatus());

        // when
        mvc.perform(get("/api/teams/" + teamId)
                .headers(headers)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//                .andExpect(jsonPath("$.teamId").value(teamId))
//                .andExpect(jsonPath("$.inviteStatus").value(inviteStatus));
    }

    @DisplayName("팀원이 팀 조회 - 성공")
    @Test
    public void getTeamByTeamMate_succeed() throws Exception {
        // given
        membersSetUp();
        getAccessToken(teamLeader);
        ArrayList<Long> ids = createTeam(teamLeader);
        Long teamId = ids.get(0);
        Long teamSettingId = ids.get(1);
        TeamSetting teamSetting1 = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("팀 구성이 존재하지 않음"));
        log.info("[ teamSetting Id : {} / teamLeader의 상태 : {} ]", teamSettingId, teamSetting1.getInviteStatus());
        TeamSetting teamSetting2 = sendInvitation(teamId, teamSettingId, teamMate.getName());
        log.info("[ teamSetting Id : {} / teamMate의 상태 : {} ]", teamSetting2.getId(), teamSetting2.getInviteStatus());
        getAccessToken(teamMate);
        boolean accept = true;
        TeamSetting teamSetting3 = respondToInvitation(teamId, teamMate.getId(), accept, teamSetting2.getId());
        log.info("[ teamSetting Id : {} / teamMate의 상태 : {} ]", teamSetting3.getId(), teamSetting3.getInviteStatus());

        // when
        mvc.perform(get("/api/teams/" + teamId)
                        .headers(headers)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // 아래 테스트는 성공하지만 (토큰으로 사용자 인증 후, 권한 없음이 판명되는 것이 아닌) 토큰 자체에 문제가 있어서 예외가 던져지면서 테스트가 성공하므로
    // 다른 테스트 방법을 찾아야 한다.
    @DisplayName("팀원이 아닌 사용자가 팀 조회 - 실패(권한없음)")
    @Test
    public void getTeamByNotTeamMemeber_fail() throws Exception {
        // given
        membersSetUp();
        getAccessToken(teamLeader);
        ArrayList<Long> ids = createTeam(teamLeader);
        Long teamId = ids.get(0);
        Long teamSettingId = ids.get(1);
        TeamSetting teamSetting1 = teamSettingRepository.findById(teamSettingId)
                .orElseThrow(() -> new EntityNotFoundException("팀 구성이 존재하지 않음"));
        log.info("[ teamSetting Id : {} / teamLeader의 상태 : {} ]", teamSettingId, teamSetting1.getInviteStatus());
        TeamSetting teamSetting2 = sendInvitation(teamId, teamSettingId, notTeamMember.getName());
        log.info("[ teamSetting Id : {} / teamMate의 상태 : {} ]", teamSetting2.getId(), teamSetting2.getInviteStatus());
        getAccessToken(notTeamMember);
        boolean accept = false;
        TeamSetting teamSetting3 = respondToInvitation(teamId, notTeamMember.getId(), accept, teamSetting2.getId());
        log.info("[ teamSetting Id : {} / teamMate의 상태 : {} ]", teamSetting3.getId(), teamSetting3.getInviteStatus());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> teamService.getTeam(teamId));
    }

    private void membersSetUp() throws Exception {
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
        log.info("[ 멤버 이름 : {} ]", memberName);
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

    private TeamSetting respondToInvitation(Long teamId, Long inviteeId, boolean accept, Long teamSettingId) {
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

        return teamSetting;
    }

}
