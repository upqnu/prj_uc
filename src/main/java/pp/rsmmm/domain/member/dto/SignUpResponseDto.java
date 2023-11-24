package pp.rsmmm.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import pp.rsmmm.domain.member.entity.MemberInfo;

@Builder
@Getter
public class SignUpResponseDto {

    private MemberInfo memberInfo;
    private Integer status;
    private String message;

}
