package pp.rsmmm.domain.teamsetting.entity;

import lombok.Getter;

@Getter
public enum InviteStatus {

    INVITING("Team_Leader"),
    RECEIVED("초대 수신"),
    ACCEPTED("Team_Mate"),
    REFUSED("초대 거절");

    private final String status;

    InviteStatus(String status) {
        this.status = status;
    }
}
