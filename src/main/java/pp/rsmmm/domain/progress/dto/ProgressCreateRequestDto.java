package pp.rsmmm.domain.progress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressCreateRequestDto {

    private String name;

    public static ProgressCreateRequestDto of(String name) {
        return new ProgressCreateRequestDto(name);
    }
}
