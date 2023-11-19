package pp.rsmmm.domain.teamsetting.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeamSetting is a Querydsl query type for TeamSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamSetting extends EntityPathBase<TeamSetting> {

    private static final long serialVersionUID = 927831037L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeamSetting teamSetting = new QTeamSetting("teamSetting");

    public final pp.rsmmm.global.config.model.QBaseEntity _super = new pp.rsmmm.global.config.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<InviteStatus> inviteStatus = createEnum("inviteStatus", InviteStatus.class);

    public final pp.rsmmm.domain.member.entity.QMember member;

    public final pp.rsmmm.domain.team.entity.QTeam team;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTeamSetting(String variable) {
        this(TeamSetting.class, forVariable(variable), INITS);
    }

    public QTeamSetting(Path<? extends TeamSetting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeamSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeamSetting(PathMetadata metadata, PathInits inits) {
        this(TeamSetting.class, metadata, inits);
    }

    public QTeamSetting(Class<? extends TeamSetting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new pp.rsmmm.domain.member.entity.QMember(forProperty("member")) : null;
        this.team = inits.isInitialized("team") ? new pp.rsmmm.domain.team.entity.QTeam(forProperty("team")) : null;
    }

}

