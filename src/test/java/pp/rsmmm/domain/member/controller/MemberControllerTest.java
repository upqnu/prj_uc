package pp.rsmmm.domain.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import pp.rsmmm.IntegrationTest;
import pp.rsmmm.domain.member.dto.SignUpRequestDto;
import pp.rsmmm.domain.member.repository.MemberRepository;

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

    @DisplayName("회원 가입 테스트")
    @Test
    void signUp_succeed() throws Exception {
        // given
        String name = "kanban_tester";
        String password = "asdf4321";
        String email = "kanban1@email.com";

        SignUpRequestDto signUpRequestDto = SignUpRequestDto.of(name, password, email);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/member/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
        ;
    }

}