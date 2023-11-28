package pp.rsmmm.global.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pp.rsmmm.domain.member.entity.Authority;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.progress.repository.ProgressRepository;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.teamsetting.entity.InviteStatus;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;
import pp.rsmmm.domain.ticket.entity.Ticket;
import pp.rsmmm.domain.ticket.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DummyDataService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TeamSettingRepository teamSettingRepository;
    private final ProgressRepository progressRepository;
    private final TicketRepository ticketRepository;

    public void createDummyData() {
        createDummyTeamSettings();
        createDummyMembers();
    }

    private void createDummyTeamSettings() {

        Member member01 = Member.builder()
                .name("dummy_member01")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy01@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member02 = Member.builder()
                .name("dummy_member02")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy02@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member03 = Member.builder()
                .name("dummy_member03")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy03@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member04 = Member.builder()
                .name("dummy_member04")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy04@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member05 = Member.builder()
                .name("dummy_member05")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy05@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member06 = Member.builder()
                .name("dummy_member06")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy06@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member07 = Member.builder()
                .name("dummy_member07")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy07@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member08 = Member.builder()
                .name("dummy_member08")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy08@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member09 = Member.builder()
                .name("dummy_member09")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy09@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member10 = Member.builder()
                .name("dummy_member10")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy10@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member11 = Member.builder()
                .name("dummy_member11")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy11@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member12 = Member.builder()
                .name("dummy_member12")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy12@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Team team1 = Team.builder()
                .name("team1")
                .kanban("kanban1")
                .build();

        Team team2 = Team.builder()
                .name("team2")
                .kanban("kanban2")
                .build();

        Team team3 = Team.builder()
                .name("team3")
                .kanban("kanban3")
                .build();

        Team team4 = Team.builder()
                .name("team4")
                .kanban("kanban4")
                .build();

        List<TeamSetting> teamSettings = new ArrayList<>();

        TeamSetting teamSetting01 = TeamSetting.builder()
                .team(team1)
                .member(member01)
                .inviteStatus(InviteStatus.INVITING)
                .build();

        TeamSetting teamSetting02 = TeamSetting.builder()
                .team(team2)
                .member(member02)
                .inviteStatus(InviteStatus.INVITING)
                .build();

        TeamSetting teamSetting03 = TeamSetting.builder()
                .team(team3)
                .member(member03)
                .inviteStatus(InviteStatus.INVITING)
                .build();

        TeamSetting teamSetting04 = TeamSetting.builder()
                .team(team4)
                .member(member04)
                .inviteStatus(InviteStatus.INVITING)
                .build();

        TeamSetting teamSetting05 = TeamSetting.builder()
                .team(team1)
                .member(member04)
                .inviteStatus(InviteStatus.ACCEPTED)
                .build();

        TeamSetting teamSetting06 = TeamSetting.builder()
                .team(team1)
                .member(member06)
                .inviteStatus(InviteStatus.ACCEPTED)
                .build();

        TeamSetting teamSetting07 = TeamSetting.builder()
                .team(team1)
                .member(member07)
                .inviteStatus(InviteStatus.RECEIVED)
                .build();

        TeamSetting teamSetting08 = TeamSetting.builder()
                .team(team1)
                .member(member08)
                .inviteStatus(InviteStatus.REFUSED)
                .build();

        TeamSetting teamSetting09 = TeamSetting.builder()
                .team(team2)
                .member(member09)
                .inviteStatus(InviteStatus.ACCEPTED)
                .build();

        TeamSetting teamSetting10 = TeamSetting.builder()
                .team(team2)
                .member(member01)
                .inviteStatus(InviteStatus.ACCEPTED)
                .build();

        TeamSetting teamSetting11 = TeamSetting.builder()
                .team(team3)
                .member(member10)
                .inviteStatus(InviteStatus.ACCEPTED)
                .build();

        TeamSetting teamSetting12 = TeamSetting.builder()
                .team(team3)
                .member(member04)
                .inviteStatus(InviteStatus.ACCEPTED)
                .build();

        TeamSetting teamSetting13 = TeamSetting.builder()
                .team(team4)
                .member(member11)
                .inviteStatus(InviteStatus.ACCEPTED)
                .build();

        TeamSetting teamSetting14 = TeamSetting.builder()
                .team(team4)
                .member(member05)
                .inviteStatus(InviteStatus.ACCEPTED)
                .build();

        TeamSetting teamSetting15 = TeamSetting.builder()
                .team(team4)
                .member(member12)
                .inviteStatus(InviteStatus.RECEIVED)
                .build();

        teamSettings.addAll(teamSettingRepository.saveAll(List.of(
                teamSetting01, teamSetting02, teamSetting03, teamSetting04, teamSetting05,
                teamSetting06, teamSetting07, teamSetting08, teamSetting09, teamSetting10,
                teamSetting11, teamSetting12, teamSetting13, teamSetting14, teamSetting15
        )));

        List<Progress> progresses = new ArrayList<>();

        Progress progress1 = Progress.builder()
                .name("Progress_1")
                .numbering(1)
                .team(team1)
                .ticketList(new ArrayList<>())
                .build();

        Progress progress2 = Progress.builder()
                .name("Progress_2")
                .numbering(2)
                .team(team1)
                .ticketList(new ArrayList<>())
                .build();

        progresses.addAll(progressRepository.saveAll(List.of(progress1, progress2)));

        List<Ticket> tickets = new ArrayList<>();

        Ticket ticket_A = Ticket.builder()
                .title("A")
                .numbering(1)
                .tag("frontend")
                .personHour(4.5)
                .dueDate(LocalDateTime.of(2023,11,27,11,30, 0))
                .progress(progress1)
                .memberId(1L)
                .build();

        Ticket ticket_B = Ticket.builder()
                .title("B")
                .numbering(2)
                .tag("backend")
                .personHour(3.5)
                .dueDate(LocalDateTime.of(2023,11,27,15,30, 0))
                .progress(progress1)
                .memberId(1L)
                .build();

        Ticket ticket_C = Ticket.builder()
                .title("C")
                .numbering(3)
                .tag("database")
                .personHour(2.5)
                .dueDate(LocalDateTime.of(2023,11,28,11,30, 0))
                .progress(progress1)
                .memberId(1L)
                .build();

        Ticket ticket_D = Ticket.builder()
                .title("D")
                .numbering(1)
                .tag("PM")
                .personHour(1.5)
                .dueDate(LocalDateTime.of(2023,11,28,15,30, 0))
                .progress(progress2)
                .memberId(1L)
                .build();

        Ticket ticket_E = Ticket.builder()
                .title("E")
                .numbering(2)
                .tag("CTO")
                .personHour(1.0)
                .dueDate(LocalDateTime.of(2023,11,29,11,30, 0))
                .progress(progress2)
                .memberId(1L)
                .build();

        tickets.addAll(ticketRepository.saveAll(List.of(
                ticket_A, ticket_B, ticket_C, ticket_D, ticket_E
        )));
    }

    protected void createDummyMembers() {
        List<Member> members = new ArrayList<>();

        Member member13 = Member.builder()
                .name("dummy_member13")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy13@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member14 = Member.builder()
                .name("dummy_member14")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy14@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member15 = Member.builder()
                .name("dummy_member15")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy15@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member16 = Member.builder()
                .name("dummy_member16")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy16@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member17 = Member.builder()
                .name("dummy_member17")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy17@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member18 = Member.builder()
                .name("dummy_member18")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy18@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member19 = Member.builder()
                .name("dummy_member19")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy19@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        Member member20 = Member.builder()
                .name("dummy_member20")
                .password(passwordEncoder.encode("qwer1234"))
                .email("dummy20@email.com")
                .authority(Authority.ROLE_MEMBER)
                .build();

        members.addAll(memberRepository.saveAll(List.of(
                member13, member14, member15, member16, member17, member18, member19, member20
        )));
    }

}
