package pp.rsmmm.domain.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RenewTokenRequestDto {
    private String refreshToken;
}
