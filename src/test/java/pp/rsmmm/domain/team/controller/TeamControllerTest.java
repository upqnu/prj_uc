package pp.rsmmm.domain.team.controller;

import jakarta.persistence.EntityNotFoundException;
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
import pp.rsmmm.global.config.jwt.TokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pp.rsmmm.domain.member.entity.Authority.ROLE_MEMBER;

class TeamControllerTest extends IntegrationTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    TeamRepository teamRepository;

    private static HttpHeaders headers;

    private Member teamLeader;
    private Member teamMate;
    private Member notTeamMember;

    @BeforeEach
    void setUp() throws Exception {
        teamLeader = createMember("teamLeader");
        teamMate = createMember("teamMate");
        notTeamMember = createMember("notTeamMember");
        getAccessToken(teamLeader);
    }

    @DisplayName("팀 생성 - 성공")
    @Test
    public void createTeam_succeed() throws Exception {
        // given
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

        Team team = teamRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("팀이 읎다...")); // test에서 생성된 팀은 1개여서 teamId = 1L 이다. 실제 1L이라는 id를 가진 팀의 존재여부를 확인.

        System.out.println(team.getName());
    }

    @DisplayName("팀원 초대1 - 성공")
    @Test
    public void inviteMate1_succeed() throws Exception {
        // given
        String invitedMemberName = teamMate.getName();
        System.out.println("<이름> : " + invitedMemberName);

        Long teamId = 1L;
        Team team = teamRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("팀이 읎다...")); // test에서 생성된 팀은 1개여서 teamId = 1L 이다. 실제 1L이라는 id를 가진 팀의 존재여부를 확인.
        System.out.println("<team> : " + String.valueOf(team));

        // when
        mvc.perform(post("/api/teams/" + teamId + "/invite")
                .headers(headers)
                .param("invitedMemberName", invitedMemberName)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("팀원 초대2 - 성공")
    @Test
    public void inviteMate2_succeed() throws Exception {
        // given
        String invitedMemberName2 = notTeamMember.getName();
        Long teamId = 1L;

        // when
        mvc.perform(post("/api/teams/" + teamId + "/invite")
                        .headers(headers)
                        .param("invitedMemberName", invitedMemberName2)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("팀원 초대에 수락 - 성공")
    @Test
    public void respondToInvitationTest() throws Exception {
        // given
        getAccessToken(teamMate);
        Long teamId = 1L;
        Team team = teamRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("팀이 읎다...")); // test에서 생성된 팀은 1개여서 teamId = 1L 이다. 실제 1L이라는 id를 가진 팀의 존재여부를 확인.
        System.out.println("<team> : " + String.valueOf(team));

        Long inviteeId = teamMate.getId();
        Member member = memberRepository.findById(inviteeId)
                .orElseThrow(() -> new EntityNotFoundException("멤버가 없다"));
        System.out.println("<member> : " + String.valueOf(member));

        boolean accept = true;

        // when
        mvc.perform(post("/api/teams/" + teamId + "/invitation/" + inviteeId)
                .headers(headers)
                .param("accept", String.valueOf(accept))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    public void getTeamTest() throws Exception {
//        // given
//        Long teamId = 1L;
//
//        // when
//        ResultActions resultActions = mvc.perform(get("/api/teams/" + teamId));
//
//        // then
//        resultActions.andExpect(status().isOk());
//    }


    void getAccessToken(Member member) throws Exception {
        System.out.println("[member] : " + String.valueOf(member));
        String tokenType = "access";
        String accessToken = tokenProvider.issueToken(member, tokenType);

        // 토큰이 정상적으로 생성되었는지 확인
        assertNotNull(accessToken);

        // 토큰이 요청 헤더에 정상적으로 로드되는지 확인
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);  // Bearer는 토큰 타입이며, 필요에 따라 다르게 설정할 수 있다.
        System.out.println("[Headers] ; " + String.valueOf(headers));
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
}
