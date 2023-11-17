package pp.rsmmm.domain.member.controller;

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
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = memberService.signUp(signUpRequestDto);
        return ResponseEntity.status(signUpResponseDto.getStatus()).body(signUpResponseDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponseDto> singIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        return ResponseEntity.ok(memberService.signIn(signInRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RenewTokenResponseDto> renewToken(@RequestBody RenewTokenRequestDto renewTokenRequestDto) {
        String renewedAccessToken = memberService.renewToken(renewTokenRequestDto.getRefreshToken());
        return ResponseEntity.ok(RenewTokenResponseDto.of(renewedAccessToken));
    }
}
