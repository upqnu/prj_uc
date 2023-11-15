package pp.rsmmm.domain.teamsetting.entity;

import jakarta.persistence.*;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.global.config.model.BaseEntity;

import java.util.List;

@Entity
public class TeamSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private boolean isLeader;

    private boolean isMate;

    @OneToMany
    private List<Progress> progressList;
}
