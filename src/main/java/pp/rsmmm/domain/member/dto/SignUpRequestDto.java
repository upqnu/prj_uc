package pp.rsmmm.domain.member.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequestDto {

    @Size(min = 6, message = "아이디는 6자 이상 입력해주세요")
    @Pattern(regexp = "^[0-9a-zA-Z]*$", message = "아이디는 숫자와 영문만 사용할 수 있습니다")
    private String name;

    @NotBlank(message = "패스워드를 입력해 주세요.")
    @Pattern(regexp = "^(?!\\d+$).+", message = "숫자로만 이뤄진 비밀번호는 사용할 수 없습니다")
    private String password;

    @NotNull
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    public static SignUpRequestDto of(String name, String password, String email) {
        return new SignUpRequestDto(name, password, email);
    }

}
