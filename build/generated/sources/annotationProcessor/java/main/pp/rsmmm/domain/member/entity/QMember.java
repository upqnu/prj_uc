package pp.rsmmm.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -459495605L;

    public static final QMember member = new QMember("member1");

    public final pp.rsmmm.global.config.model.QBaseEntity _super = new pp.rsmmm.global.config.model.QBaseEntity(this);

    public final EnumPath<Authority> authority = createEnum("authority", Authority.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<pp.rsmmm.domain.teamsetting.entity.TeamSetting, pp.rsmmm.domain.teamsetting.entity.QTeamSetting> teamSettingList = this.<pp.rsmmm.domain.teamsetting.entity.TeamSetting, pp.rsmmm.domain.teamsetting.entity.QTeamSetting>createList("teamSettingList", pp.rsmmm.domain.teamsetting.entity.TeamSetting.class, pp.rsmmm.domain.teamsetting.entity.QTeamSetting.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

