package pp.rsmmm.domain.team.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private HttpHeaders headers;

    @BeforeEach
    void getAccessToken() throws Exception {
        Member member = createMember();
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
//        return headers;
    }

    Member createMember() {
        Member member = Member.builder()
                .name("tokenTester")
                .password(passwordEncoder.encode("zxcv1234"))
                .email("tokenTester@email.com")
                .authority(ROLE_MEMBER)
                .build();

        memberRepository.save(member);

        return member;
    }

    @DisplayName("팀 생성 - 성공")
    @Test
    public void createTeam_succeed() throws Exception {
        // given
        TeamCreateRequestDto teamCreateRequestDto = new TeamCreateRequestDto("testTeam", "testKanban");
        System.out.println("<teamCreateRequestDto>" + String.valueOf(teamCreateRequestDto));

        // when
//        HttpHeaders headers = getAccessToken();

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

        System.out.println("[team]");
    }

    @Test
    public void getTeamTest() throws Exception {
        // given
        Long teamId = 1L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/teams/" + teamId));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void inviteMateTest() throws Exception {
        // given
        Long teamId = 1L;
        String invitedMemberName = "testMember";

        // when
        ResultActions resultActions = mvc.perform(post("/api/teams/" + teamId + "/invite")
                .param("invitedMemberName", invitedMemberName));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void respondToInvitationTest() throws Exception {
        // given
        Long teamId = 1L;
        Long inviteeId = 1L;
        boolean accept = true;

        // when
        ResultActions resultActions = mvc.perform(post("/api/teams/" + teamId + "/invitation/" + inviteeId)
                .param("accept", String.valueOf(accept)));

        // then
        resultActions.andExpect(status().isOk());
    }
}

//    @BeforeEach
//    void init() {
//        Member member = Member.builder()
//                .name("teamTester")
//                .password(passwordEncoder.encode("vcxz1234"))
//                .email("teamTester@email.com")
//                .authority(ROLE_MEMBER)
//                .build();
//
//        memberRepository.save(member);
//
//        String tokenType = "access";
//        String token = tokenProvider.issueToken(member, tokenType);
//    }
//
//    @DisplayName("팀(Team) 생성 및 팀구성(TeamSetting) 생성 - 성공")
//    @Test
//    void createTeam_succeed() throws Exception {
//        // given
//        String token = mvc.perform(MockMvcRequestBuilders.post("/api/members/sign-in")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"username\":\"teamTester\",\"password\":\"vcxz1234\"}"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn().getResponse().getHeader("Authorization");
//
//        assert token != null;
//
//        // (2) team 생성을 위한 입력
//        String name = "testTeam";
//        String kanban = "testKanban";
//        TeamCreateRequestDto teamCreateRequestDto = TeamCreateRequestDto.of(name, kanban);
//
//        // when
//        mvc.perform(MockMvcRequestBuilders.post("/api/teams/create")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(teamCreateRequestDto))
//                        .andExpect(MockMvcResultMatchers.status().isCreated())
//        );
//
//    }
//
//    Member createMember() {
//        Member member = Member.builder()
//                .name("teamTester")
//                .password(passwordEncoder.encode("vcxz1234"))
//                .email("teamTester@email.com")
//                .authority(ROLE_MEMBER)
//                .build();
//
//        memberRepository.save(member);
//
//        return member;
//    }
//
//    @Test
//    @Order(1)
//    String singIn_pre() throws Exception {
//        // given
//        Member member = Member.builder()
//                .name("kanban_tester")
//                .password(passwordEncoder.encode("asdf4321"))
//                .email("kanban1@email.com")
//                .authority(Authority.ROLE_MEMBER)
//                .build();
//
//        memberRepository.save(member);
//
//        SignInRequestDto signInRequestDto = SignInRequestDto.of(member.getName(), "asdf4321");
//
//        // when
//        mvc.perform(
//                        post("/api/members/sign-in")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(signInRequestDto))
//                )
//                // then
//                .andDo(print())
//                .andExpect(jsonPath("$.accessToken").isNotEmpty());
//
//        return jsonPath("$.accessToken").toString();
//    }

//}