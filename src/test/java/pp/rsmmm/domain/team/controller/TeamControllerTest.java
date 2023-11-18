package pp.rsmmm.domain.team.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import pp.rsmmm.IntegrationTest;
import pp.rsmmm.domain.member.dto.SignInRequestDto;
import pp.rsmmm.domain.member.dto.SignUpRequestDto;
import pp.rsmmm.domain.member.entity.Authority;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.team.dto.TeamCreateRequestDto;
import pp.rsmmm.global.config.jwt.TokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamControllerTest extends IntegrationTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Test
    @Order(1)
    String singIn_pre() throws Exception {
        // given
        Member member = Member.builder()
                .name("kanban_tester")
                .password(passwordEncoder.encode("asdf4321"))
                .email("kanban1@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        memberRepository.save(member);

        SignInRequestDto signInRequestDto = SignInRequestDto.of(member.getName(), "asdf4321");

        // when
        mvc.perform(
                        post("/api/members/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signInRequestDto))
                )
                // then
                .andDo(print())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());

        return jsonPath("$.accessToken").toString();
    }

    @DisplayName("팀(Team) 생성 및 팀구성(TeamSetting) 생성 테스트")
    @Test
    void createTeam_succeed() throws Exception {
        // given
        String token = singIn_pre();
        System.out.println("token : " + String.valueOf(token));
        String teamName = "1st testing team";
        String kanban = "첫번째 팀 칸반보드";

        TeamCreateRequestDto teamCreateRequestDto = TeamCreateRequestDto.of(teamName, kanban);

        // when
        mvc.perform(
                post("/api/teams/create")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamCreateRequestDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists());

    }
}