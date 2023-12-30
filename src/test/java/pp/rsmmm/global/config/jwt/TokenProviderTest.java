package pp.rsmmm.global.config.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pp.rsmmm.IntegrationTest;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;

import java.security.Key;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pp.rsmmm.domain.member.entity.Authority.ROLE_MEMBER;

@Slf4j
class TokenProviderTest extends IntegrationTest {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    Key key;

    @Value("${jwt.secret}")
    private String secretKey;

    private final TokenProvider invlaidTokenProvider =
            new TokenProvider("invalidSecretKeyIsNeededHere", 1800L, 604800L);

    @DisplayName("JWT signature 생성 - 성공")
    @Test
    void afterPropertiesSet() {
        // when
        byte[] decodedKeyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decodedKeyBytes);

        // then
        log.info("JWT signature : {}", String.valueOf(key));
        assertThat(key).isNotNull();
    }

    @DisplayName("토큰 생성 - 성공")
    @Test
    void issueToken_succeed() {
        // given
        String tokenType = "access";
        Member member = createMember();

        // when
        String accessToken = tokenProvider.issueToken(member, tokenType);

        // then
        log.info("액세스 토큰 : {}", accessToken);
        assertThat(accessToken).isNotNull();
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

    @DisplayName("토큰 검증 - 성공")
    @Test
    void validateToken_succeed() {
        // given
        Member member = createMember();
        String tokenType = "access";
        String accessToken = tokenProvider.issueToken(member, tokenType);

        // when
        boolean isValidated = tokenProvider.validateToken(accessToken);

        // then
        assertThat(isValidated).isTrue();
    }

    @DisplayName("토큰 검증 - 실패")
    @Test
    void validateToken_fail() {
        // given
        Member member = createMember();
        String tokenType = "access";
        String accessToken = tokenProvider.issueToken(member, tokenType);

        // when
        boolean isValidated = invlaidTokenProvider.validateToken(accessToken);

        // then
        assertThat(isValidated).isFalse();
    }

    @DisplayName("인증정보 확인 - 성공")
    @Test
    void getAuthentication() {
        // given
        String tokenType = "access";
        Member member = createMember();
        String accessToken = tokenProvider.issueToken(member, tokenType);

        // when
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        // then
        log.info("authentication : {}", String.valueOf(authentication));
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("tokenTester");
        assertThat(authentication.getAuthorities()).toString().contains("ROLE_MEMBER");
    }

    @DisplayName("발급된 토큰을 통해 사용자 이름 확인 - 성공")
    @Test
    void getMemberNameFromToken() {
        // given
        String tokenType = "access";
        Member member = createMember();
        log.info("[ name : {}", member.getName());
        String accessToken = tokenProvider.issueToken(member, tokenType);
        assertNotNull(accessToken);

        // HttpServletRequest mock 생성
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + accessToken);

        // RequestContextHolder에 mock request 설정
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // when
        String memberName = tokenProvider.getMemberNameFromToken();

        // then
        log.info("MemberName : {}", memberName);
        assertThat(memberName).isEqualTo("tokenTester");
    }

}