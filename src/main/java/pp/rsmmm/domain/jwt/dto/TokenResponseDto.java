package pp.rsmmm.domain.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {

    private String accessToken;

    public static TokenResponseDto of(String newAccessToken) {
        return new TokenResponseDto(newAccessToken);
    }
}
