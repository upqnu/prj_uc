package pp.rsmmm.domain.member.entity;

import lombok.Getter;

@Getter
public enum Authority {
    ROLE_MEMBER("MEMBER"),
    ROLE_ADMIN("ADMIN");

    private final String status;

    Authority(String status) {
        this.status = status;
    }
}
