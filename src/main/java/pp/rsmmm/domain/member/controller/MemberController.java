package pp.rsmmm.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.rsmmm.domain.jwt.dto.RenewTokenRequestDto;
import pp.rsmmm.domain.jwt.dto.RenewTokenResponseDto;
import pp.rsmmm.domain.jwt.dto.TokenResponseDto;
import pp.rsmmm.domain.member.Service.MemberService;
import pp.rsmmm.domain.member.dto.SignInRequestDto;
import pp.rsmmm.domain.member.dto.SignUpRequestDto;
import pp.rsmmm.domain.member.dto.SignUpResponseDto;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     * @param signUpRequestDto
     * @return
     */
    @Operation(summary = "회원 가입 요청", description = "입력된 회원 정보를 저장합니다")
    @Parameter(name = "signUpRequestDto", description = "회원 가입 시, name/password/email 필수 입력")
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = memberService.signUp(signUpRequestDto);
        return ResponseEntity.status(signUpResponseDto.getStatus()).body(signUpResponseDto);
    }

    /**
     * 로그인 & 액세스 토큰 발급
     * @param signInRequestDto
     * @return
     */
    @Operation(summary = "로그인", description = "로그인 & 액세스 토큰 발급")
    @Parameter(name = "signInRequestDto", description = "memberName/password 입력하여 로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponseDto> singIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        return ResponseEntity.ok(memberService.signIn(signInRequestDto));
    }

    /**
     * 리프레시 토큰 통한 액세스 토큰 유효기한 연장
     * @param renewTokenRequestDto
     * @return
     */
    @Operation(summary = "토큰 재발급", description = "유효기간 만료 시, 액세스 토큰 재발급")
    @Parameter(name = "RenewTokenRequestDto", description = "'refreshToken' 입력하여 액세스 토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<RenewTokenResponseDto> renewToken(@RequestBody RenewTokenRequestDto renewTokenRequestDto) {
        String renewedAccessToken = memberService.renewToken(renewTokenRequestDto.getRefreshToken());
        return ResponseEntity.ok(RenewTokenResponseDto.of(renewedAccessToken));
    }
}
