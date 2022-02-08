package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArchive is a Querydsl query type for Archive
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QArchive extends EntityPathBase<Archive> {

    private static final long serialVersionUID = -1301903680L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArchive archive = new QArchive("archive");

    public final NumberPath<Integer> archiveId = createNumber("archiveId", Integer.class);

    public final StringPath archiveName = createString("archiveName");

    public final EnumPath<ArchiveType> archiveType = createEnum("archiveType", ArchiveType.class);

    public final com.ssafy.db.entity.meeting.QMeeting meeting;

    public final StringPath path = createString("path");

    public final QUser user;

    public QArchive(String variable) {
        this(Archive.class, forVariable(variable), INITS);
    }

    public QArchive(Path<? extends Archive> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArchive(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArchive(PathMetadata metadata, PathInits inits) {
        this(Archive.class, metadata, inits);
    }

    public QArchive(Class<? extends Archive> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meeting = inits.isInitialized("meeting") ? new com.ssafy.db.entity.meeting.QMeeting(forProperty("meeting"), inits.get("meeting")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

