package pp.rsmmm.domain.ticket.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicket is a Querydsl query type for Ticket
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicket extends EntityPathBase<Ticket> {

    private static final long serialVersionUID = 1490401455L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicket ticket = new QTicket("ticket");

    public final pp.rsmmm.global.config.model.QBaseEntity _super = new pp.rsmmm.global.config.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> dueDate = createDateTime("dueDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Integer> numbering = createNumber("numbering", Integer.class);

    public final NumberPath<Double> personHour = createNumber("personHour", Double.class);

    public final pp.rsmmm.domain.progress.entity.QProgress progress;

    public final StringPath tag = createString("tag");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTicket(String variable) {
        this(Ticket.class, forVariable(variable), INITS);
    }

    public QTicket(Path<? extends Ticket> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicket(PathMetadata metadata, PathInits inits) {
        this(Ticket.class, metadata, inits);
    }

    public QTicket(Class<? extends Ticket> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.progress = inits.isInitialized("progress") ? new pp.rsmmm.domain.progress.entity.QProgress(forProperty("progress"), inits.get("progress")) : null;
    }

}

