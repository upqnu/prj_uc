package pp.rsmmm.domain.ticket.controller;

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
import pp.rsmmm.domain.ticket.dto.TicketCreateRequestDto;
import pp.rsmmm.domain.ticket.dto.TicketModifyDto;
import pp.rsmmm.domain.ticket.dto.TicketOrderModifyDto;
import pp.rsmmm.domain.ticket.entity.Ticket;
import pp.rsmmm.domain.ticket.repository.TicketRepository;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class TicketControllerTest extends IntegrationTest {
    /*
    이 테스트 클래스는 스프링 서버 구동과 동시에 생성되는 dummy data를 대상으로 진행됨.
    teamId = 1L인 팀은 (1) 이름이 "dummy_member01" 사용자가 팀원, "dummy_member04" 사용자가 팀원이며
    (2) progressId가 1L, 2L인 진행상항을 포함하고 있으며
    (3) progressId가 1L / 2L인 진행상황은 각각 이름이 ticket_A, ticket_B, ticket_C / ticket_D, ticket_E인 티켓을 포함하고 있다
    는 것을 테스트에 활용
     */

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TicketRepository ticketRepository;

    private static HttpHeaders headers;
    private Member dummy_member01;
    private Member dummy_member04;

    @DisplayName("팀장이 티켓 생성 - 성공")
    @Test
    void createTicketByTeamLeader_succeed() throws Exception {
        // given
        dummy_member01 = memberRepository.findByName("dummy_member01")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member01);
        Long teamId = 1L;
        Long progressId = 1L;
        TicketCreateRequestDto ticketCreateRequestDto = new TicketCreateRequestDto("test", "middleEnd", 10.0, LocalDateTime.now());
        log.info("<progressCreateRequestDto>" + String.valueOf(ticketCreateRequestDto));

        // when
        mvc.perform(post("/api/teams/" + teamId + "/progresses/" + progressId + "/tickets/create")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketCreateRequestDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @DisplayName("팀원이 티켓 생성 - 성공")
    @Test
    void createTicketByTeamMate_succeed() throws Exception {
        // given
        dummy_member04 = memberRepository.findByName("dummy_member04")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member04);
        Long teamId = 1L;
        Long progressId = 1L;
        TicketCreateRequestDto ticketCreateRequestDto = new TicketCreateRequestDto("test", "middleEnd", 10.0, LocalDateTime.now());
        log.info("<progressCreateRequestDto>" + String.valueOf(ticketCreateRequestDto));

        // when
        mvc.perform(post("/api/teams/" + teamId + "/progresses/" + progressId + "/tickets/create")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateRequestDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @DisplayName("팀장이 티켓 삭제 - 성공")
    @Test
    void deleteTicketByTeamLeader_succeed() throws Exception {
        // given
        dummy_member01 = memberRepository.findByName("dummy_member01")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member01);
        Long teamId = 1L;
        Long progressId = 2L;
        Long ticketId = 3L;

        // when
        mvc.perform(delete("/api/teams/" + teamId + "/progresses/" + progressId + "/tickets/" + ticketId)
                .headers(headers)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 삭제된 티켓을 찾아봄
                    Optional<Ticket> deletedTicket = ticketRepository.findById(ticketId);

                    // 찾을 수 없다면 테스트 통과
                    assertFalse(deletedTicket.isPresent());
                });
    }

    @DisplayName("팀원이 티켓 삭제 - 성공")
    @Test
    void deleteTicketByTeamMate_succeed() throws Exception {
        // given
        dummy_member04 = memberRepository.findByName("dummy_member04")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member04);
        Long teamId = 1L;
        Long progressId = 2L;
        Long ticketId = 3L;

        // when
        mvc.perform(delete("/api/teams/" + teamId + "/progresses/" + progressId + "/tickets/" + ticketId)
                        .headers(headers)
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 삭제된 티켓을 찾아봄
                    Optional<Ticket> deletedTicket = ticketRepository.findById(ticketId);

                    // 찾을 수 없다면 테스트 통과
                    assertFalse(deletedTicket.isPresent());
                });
    }

    @DisplayName("팀장이 티켓 수정 - 성공")
    @Test
    void modifyTicketByTeamLeader_succeed() throws Exception {
        // given
        dummy_member01 = memberRepository.findByName("dummy_member01")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member01);
        Long teamId = 1L;
        Long progressId = 2L;
        Long ticketId = 2L;

        TicketModifyDto ticketModifyDto = new TicketModifyDto();
        ticketModifyDto.setTitle("testB");
        ticketModifyDto.setTag("testBackend");
        ticketModifyDto.setPersonHour(35.0);
        ticketModifyDto.setDueDate(LocalDateTime.now());

        // when
        mvc.perform(put("/api/teams/" + teamId + "/progresses/" + progressId + "/tickets/" + ticketId)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketModifyDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 수정된 티켓을 찾음
                    Ticket modifiedTicket = ticketRepository.findById(ticketId)
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));

                    // 티켓 내용이 바뀌었는지 확인
                    Assertions.assertEquals("testB", modifiedTicket.getTitle());
                    Assertions.assertEquals("testBackend", modifiedTicket.getTag());
                    Assertions.assertEquals(35.0, modifiedTicket.getPersonHour());
                    LocalDateTime now = LocalDateTime.now();
                    // 테스트를 실행하는 시간과 getDueDate()의 결과가 최대 1초 이내로 일치하는지를 확인
                    Assertions.assertTrue(Duration.between(now, modifiedTicket.getDueDate()).getSeconds() <= 1);
                });
    }

    @DisplayName("팀원이 티켓 수정 - 성공")
    @Test
    void modifyTicketByTeamMate_succeed() throws Exception {
        // given
        dummy_member04 = memberRepository.findByName("dummy_member04")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member04);
        Long teamId = 1L;
        Long progressId = 2L;
        Long ticketId = 2L;

        TicketModifyDto ticketModifyDto = new TicketModifyDto();
        ticketModifyDto.setTitle("testB");
        ticketModifyDto.setTag("testBackend");
        ticketModifyDto.setPersonHour(35.0);
        ticketModifyDto.setDueDate(LocalDateTime.now());

        // when
        mvc.perform(put("/api/teams/" + teamId + "/progresses/" + progressId + "/tickets/" + ticketId)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketModifyDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 수정된 티켓을 찾음
                    Ticket modifiedTicket = ticketRepository.findById(ticketId)
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));

                    // 티켓 내용이 바뀌었는지 확인
                    Assertions.assertEquals("testB", modifiedTicket.getTitle());
                    Assertions.assertEquals("testBackend", modifiedTicket.getTag());
                    Assertions.assertEquals(35.0, modifiedTicket.getPersonHour());
                    LocalDateTime now = LocalDateTime.now();
                    // 테스트를 실행하는 시간과 getDueDate()의 결과가 최대 1초 이내로 일치하는지를 확인
                    Assertions.assertTrue(Duration.between(now, modifiedTicket.getDueDate()).getSeconds() <= 1);
                });
    }

    @DisplayName("팀장이 티켓 순서 수정 - 성공")
    @Test
    void modifyTicketOrderByTeamLeader_succeed() throws Exception {
        // given
        dummy_member01 = memberRepository.findByName("dummy_member01")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member01);
        Long teamId = 1L;
        Long progressId = 1L;

        Ticket ticket1 = ticketRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
        Ticket ticket2 = ticketRepository.findById(2L)
                .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));

        TicketOrderModifyDto ticketOrderModifyDto = new TicketOrderModifyDto();
        ticketOrderModifyDto.setProgressNum(1);
        ticketOrderModifyDto.setTicketNum(2);

        // when
        mvc.perform(patch("/api/teams/" + teamId + "/progresses/" + progressId + "/tickets/" + ticket1.getId())
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketOrderModifyDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 수정된 티켓을 찾음
                    Ticket modifiedTicket1 = ticketRepository.findById(ticket1.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
                    Ticket modifiedTicket2 = ticketRepository.findById(ticket2.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));

                    // 티켓 순서가 바뀌었는지 확인
                    Assertions.assertEquals(2, modifiedTicket1.getNumbering());
                    Assertions.assertEquals(1, modifiedTicket2.getNumbering());

                    log.info("[ ticket_A - numbering : {} ]", modifiedTicket1.getNumbering());
                    log.info("[ ticket_B - numbering : {} ]", modifiedTicket2.getNumbering());
                });
    }

    @DisplayName("팀원이 티켓 순서 수정 - 성공")
    @Test
    void modifyTicketOrderByTeamMate_succeed() throws Exception {
        // given
        dummy_member04 = memberRepository.findByName("dummy_member04")
                .orElseThrow(() -> new EntityNotFoundException());
        getAccessToken(dummy_member04);
        Long teamId = 1L;
        Long progressId = 1L;

        Ticket ticket1 = ticketRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
        Ticket ticket2 = ticketRepository.findById(2L)
                .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
        Ticket ticket3 = ticketRepository.findById(3L)
                .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
        Ticket ticket4 = ticketRepository.findById(4L)
                .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
        Ticket ticket5 = ticketRepository.findById(5L)
                .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));

        TicketOrderModifyDto ticketOrderModifyDto = new TicketOrderModifyDto();
        ticketOrderModifyDto.setProgressNum(2);
        ticketOrderModifyDto.setTicketNum(2);

        // when
        mvc.perform(patch("/api/teams/" + teamId + "/progresses/" + progressId + "/tickets/" + ticket1.getId())
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketOrderModifyDto))
                )
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // 데이터베이스에서 수정된 티켓을 찾음
                    Optional<Ticket> deletedTicket1 = ticketRepository.findById(ticket1.getId());
                    Ticket modifiedTicket6 = ticketRepository.findById(6L)
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
                    Ticket modifiedTicket2 = ticketRepository.findById(ticket2.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
                    Ticket modifiedTicket3 = ticketRepository.findById(ticket3.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
                    Ticket modifiedTicket4 = ticketRepository.findById(ticket4.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));
                    Ticket modifiedTicket5 = ticketRepository.findById(ticket5.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 티켓을 찾을 수 없다"));

                    // 티켓 순서가 바뀌었는지 확인
                    Assertions.assertFalse(deletedTicket1.isPresent());
                    Assertions.assertEquals(2, modifiedTicket6.getNumbering());
                    Assertions.assertEquals(1, modifiedTicket2.getNumbering());
                    Assertions.assertEquals(2, modifiedTicket3.getNumbering());
                    Assertions.assertEquals(1, modifiedTicket4.getNumbering());
                    Assertions.assertEquals(3, modifiedTicket5.getNumbering());

                    log.info("[ {} - Progress : {} / numbering : {} ]", modifiedTicket2.getTitle(), modifiedTicket2.getProgress().getName(), modifiedTicket2.getNumbering());
                    log.info("[ {} - Progress : {} / numbering : {} ]", modifiedTicket3.getTitle(), modifiedTicket3.getProgress().getName(), modifiedTicket3.getNumbering());
                    log.info("[ {} - Progress : {} / numbering : {} ]", modifiedTicket4.getTitle(), modifiedTicket4.getProgress().getName(), modifiedTicket4.getNumbering());
                    log.info("[ {} - Progress : {} / numbering : {} ]", modifiedTicket6.getTitle(), modifiedTicket6.getProgress().getName(), modifiedTicket6.getNumbering());
                    log.info("[ {} - Progress : {} / numbering : {} ]", modifiedTicket5.getTitle(), modifiedTicket5.getProgress().getName(), modifiedTicket5.getNumbering());
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
}