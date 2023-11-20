package pp.rsmmm.domain.team.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = 1749776657L;

    public static final QTeam team = new QTeam("team");

    public final pp.rsmmm.global.config.model.QBaseEntity _super = new pp.rsmmm.global.config.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kanban = createString("kanban");

    public final StringPath name = createString("name");

    public final ListPath<pp.rsmmm.domain.teamsetting.entity.TeamSetting, pp.rsmmm.domain.teamsetting.entity.QTeamSetting> teamSettingList = this.<pp.rsmmm.domain.teamsetting.entity.TeamSetting, pp.rsmmm.domain.teamsetting.entity.QTeamSetting>createList("teamSettingList", pp.rsmmm.domain.teamsetting.entity.TeamSetting.class, pp.rsmmm.domain.teamsetting.entity.QTeamSetting.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTeam(String variable) {
        super(Team.class, forVariable(variable));
    }

    public QTeam(Path<? extends Team> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeam(PathMetadata metadata) {
        super(Team.class, metadata);
    }

}

