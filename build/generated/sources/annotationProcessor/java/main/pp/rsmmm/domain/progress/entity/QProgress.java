package pp.rsmmm.domain.progress.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProgress is a Querydsl query type for Progress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProgress extends EntityPathBase<Progress> {

    private static final long serialVersionUID = -541486671L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProgress progress = new QProgress("progress");

    public final pp.rsmmm.global.config.model.QBaseEntity _super = new pp.rsmmm.global.config.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> numbering = createNumber("numbering", Integer.class);

    public final pp.rsmmm.domain.team.entity.QTeam team;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProgress(String variable) {
        this(Progress.class, forVariable(variable), INITS);
    }

    public QProgress(Path<? extends Progress> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProgress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProgress(PathMetadata metadata, PathInits inits) {
        this(Progress.class, metadata, inits);
    }

    public QProgress(Class<? extends Progress> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new pp.rsmmm.domain.team.entity.QTeam(forProperty("team")) : null;
    }

}

