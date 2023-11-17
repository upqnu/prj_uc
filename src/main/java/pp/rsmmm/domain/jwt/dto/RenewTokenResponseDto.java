package pp.rsmmm.domain.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RenewTokenResponseDto {

    public String accessToken;

    public static RenewTokenResponseDto of(String renewedAccessToken) {
        return new RenewTokenResponseDto(renewedAccessToken);
    }
}
