package pp.rsmmm.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequestDto {

    @NotNull
    private String memberName;

    @NotNull
    private String password;

    public static SignInRequestDto of(String memberName, String password) {
        return new SignInRequestDto(memberName, password);
    }

}
