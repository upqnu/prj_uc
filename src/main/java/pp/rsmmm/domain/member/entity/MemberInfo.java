package pp.rsmmm.domain.member.entity;

import lombok.Getter;

@Getter
public class MemberInfo {

    private String id;
    private String name;
    private String email;
    private Authority authority;
}
