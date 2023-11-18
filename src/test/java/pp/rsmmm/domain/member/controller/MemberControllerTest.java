package pp.rsmmm.domain.member.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pp.rsmmm.IntegrationTest;
import pp.rsmmm.domain.member.dto.SignInRequestDto;
import pp.rsmmm.domain.member.dto.SignUpRequestDto;
import pp.rsmmm.domain.member.entity.Authority;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.util.Base64;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends IntegrationTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenProvider tokenProvider;

    @DisplayName("회원 가입 테스트")
    @Test
    void signUp_succeed() throws Exception {
        // given
        String name = "kanban_tester";
        String password = "asdf4321";
        String email = "kanban1@email.com";

        SignUpRequestDto signUpRequestDto = SignUpRequestDto.of(name, password, email);

        // when
        mvc.perform(
                post("/api/members/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
        ;
    }

    @DisplayName("회원 로그인 및 액세스 토큰 발급 테스트")
    @Test
    void singIn_succeed() throws Exception {
        // given
        Member member = Member.builder()
                .name("kanban_tester2")
                .password(passwordEncoder.encode("edcv4567"))
                .email("kanban2@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        memberRepository.save(member);

        SignInRequestDto signInRequestDto = SignInRequestDto.of(member.getName(), "edcv4567");

        // when
        mvc.perform(
                post("/api/members/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto))
                )
                // then
                .andDo(print())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @DisplayName("리프레시 토큰을 통한 액세스 토큰 재생성(기한연장) 테스트")
    @Test
    void renewToken_succeed() throws Exception {
        // given
        // 1. 회원가입
        Member member = Member.builder()
                .name("kanban_tester3")
                .password(passwordEncoder.encode("lkjh0987"))
                .email("kanban3@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        memberRepository.save(member);

        // 2. 로그인
        String memberName = member.getName();
        String password = "lkjh0987";

        SignInRequestDto signInRequestDto = SignInRequestDto.of(memberName, password);

        MvcResult resultOfSignIn = mvc.perform(
                        post("/api/members/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signInRequestDto))
                )
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn();

        // 3. 액세스 토큰 발급 및 확보
        String responseContentOfSignIn = resultOfSignIn.getResponse().getContentAsString();
        JsonNode responseJsonOfSignIn = objectMapper.readTree(responseContentOfSignIn);
        String accessToken = responseJsonOfSignIn.get("accessToken").asText();

        // when
        // 사용자의 리프레시 토큰을 활용하여 액세스 토큰 갱신
        String renewedAccessToken = tokenProvider.issueToken(member, "refresh");

        // then
        // 최초 발급된 액세스 토큰, 갱신되 액세스 토큰의 헤더, 페이로드, 서명을 비교하여
        // 헤더는 완전히 동일, 페이로드는 일부가 동일, 서명은 서로 다를 경우 ; 테스트 성공

        // 토큰을 헤더, 페이로드, 서명부를 분리해서 String 배열에 저장
        String[] original = accessToken.split("\\.");
        String[] renewed = renewedAccessToken.split("\\.");

        // 헤더와 서명 비교
        assertEquals(original[0], renewed[0], "헤더가 동일합니다.");
        assertNotEquals(original[2], renewed[2], "서명이 다릅니다.");

        // 페이로드 일부를 비교
        String originalPayload = new String(Base64.getDecoder().decode(original[1]));
        String renewedPayload = new String(Base64.getDecoder().decode(renewed[1]));
        assertTrue(originalPayload.startsWith(renewedPayload.substring(0, 20)), "페이로드가 처음부터 동일합니다.");
        assertTrue(originalPayload.endsWith(renewedPayload.substring(renewedPayload.length() - 20)), "페이로드가 끝에서 동일합니다.");

    }

}