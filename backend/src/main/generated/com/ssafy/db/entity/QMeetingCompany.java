package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetingCompany is a Querydsl query type for MeetingCompany
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMeetingCompany extends EntityPathBase<MeetingCompany> {

    private static final long serialVersionUID = 43049476L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetingCompany meetingCompany = new QMeetingCompany("meetingCompany");

    public final QCompany company;

    public final com.ssafy.db.entity.meeting.QMeeting meeting;

    public final NumberPath<Integer> meetingCompanyId = createNumber("meetingCompanyId", Integer.class);

    public QMeetingCompany(String variable) {
        this(MeetingCompany.class, forVariable(variable), INITS);
    }

    public QMeetingCompany(Path<? extends MeetingCompany> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetingCompany(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetingCompany(PathMetadata metadata, PathInits inits) {
        this(MeetingCompany.class, metadata, inits);
    }

    public QMeetingCompany(Class<? extends MeetingCompany> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.meeting = inits.isInitialized("meeting") ? new com.ssafy.db.entity.meeting.QMeeting(forProperty("meeting"), inits.get("meeting")) : null;
    }

}

