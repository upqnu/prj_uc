package pp.rsmmm.domain.member.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.rsmmm.domain.jwt.dto.TokenResponseDto;
import pp.rsmmm.domain.jwt.entity.RefreshToken;
import pp.rsmmm.domain.jwt.repository.RefreshTokenRepository;
import pp.rsmmm.domain.member.dto.SignInRequestDto;
import pp.rsmmm.domain.member.dto.SignUpRequestDto;
import pp.rsmmm.domain.member.dto.SignUpResponseDto;
import pp.rsmmm.domain.member.entity.Authority;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.global.config.jwt.TokenProvider;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 회원 가입 로직
     * @param signUpRequestDto
     * @return
     */
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        // 아이디 중복 체크
        if (memberRepository.existsByName(signUpRequestDto.getName())) {
            return SignUpResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("이미 존재하는 사용자명입니다. 다른 사용자명을 입력해주세요.")
                    .build();
        }

        // 회원 정보 생성 및 저장
        memberRepository.save(Member.builder()
                .name(signUpRequestDto.getName())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .email(signUpRequestDto.getEmail())
                .authority(Authority.ROLE_MEMBER)
                .build()
        );

        // 회원 가입 성공 통보
        return SignUpResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message("성공적으로 회원가입 되셨습니다.")
                .build();
    }

    /**
     * 로그인 및 액세스 토큰 발급 로직
     * @param signInRequestDto
     * @return
     */
    @Transactional
    public TokenResponseDto signIn(SignInRequestDto signInRequestDto) {

        // 사용자 찾기
        Member member = memberRepository.findByName(signInRequestDto.getMemberName())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 패스워드 일치 여부 확인
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 액세스 토큰 생성, 발급
        return TokenResponseDto.of(tokenProvider.issueToken(member, "access"));
    }

    /**
     * 리프레시 토큰 통한 액세스 토큰 유효기한 연장 로직
     * @param refreshToken
     * @return
     */
    @Transactional
    public String renewToken(String refreshToken) {

        // 1. 현재 사용자의 memberName 찾기
        String memberName = tokenProvider.getMemberNameFromToken();

        // 2. 주어진 refresh token 유효성 검사
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 3. 리프레시 토큰 생성 및 저장
        RefreshToken newRefreshToken = new RefreshToken(refreshToken, memberName);
        refreshTokenRepository.save(newRefreshToken);

        // 4. 유효기간이 연장된 새로운 액세스 토큰 발행
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        String newAccessToken = tokenProvider.issueToken(member, "access");

        return newAccessToken;
    }
}
