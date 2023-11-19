package pp.rsmmm.domain.team.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamCreateRequestDto {

    @NotBlank(message = "사용하실 팀명을 입력해 주세요.")
    private String name;

    @NotBlank(message = "사용하실 칸반보드명을 입력해 주세요.")
    private String kanban;

    public static TeamCreateRequestDto of(String name, String kanban) {
        return new TeamCreateRequestDto(name, kanban);
    }
}
