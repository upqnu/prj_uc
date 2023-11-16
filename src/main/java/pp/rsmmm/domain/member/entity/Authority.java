package pp.rsmmm.domain.member.entity;

import lombok.Getter;

@Getter
public enum Authority {

    ROLE_MEMBER("멤버"),
    ROLE_ADMIN("관리자");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }
}
