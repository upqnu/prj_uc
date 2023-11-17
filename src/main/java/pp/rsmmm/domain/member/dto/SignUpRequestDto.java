package pp.rsmmm.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pp.rsmmm.domain.member.entity.Authority;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequestDto {

    @NotBlank(message = "사용하실 사용자명을 입력해 주세요.")
    private String name;

    @NotBlank(message = "패스워드를 입력해 주세요.")
    private String password;

    @NotNull
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    public static SignUpRequestDto of(String name, String password, String email) {
        return new SignUpRequestDto(name, password, email);
    }

}
