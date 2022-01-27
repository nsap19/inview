package com.ssafy.db.entity.meeting;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeeting is a Querydsl query type for Meeting
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMeeting extends EntityPathBase<Meeting> {

    private static final long serialVersionUID = -256712314L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeeting meeting = new QMeeting("meeting");

    public final DateTimePath<java.util.Date> closeTime = createDateTime("closeTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> endTime = createDateTime("endTime", java.util.Date.class);

    public final com.ssafy.db.entity.QIndustry industry;

    public final ListPath<com.ssafy.db.entity.MeetingCompany, com.ssafy.db.entity.QMeetingCompany> meetingCompanies = this.<com.ssafy.db.entity.MeetingCompany, com.ssafy.db.entity.QMeetingCompany>createList("meetingCompanies", com.ssafy.db.entity.MeetingCompany.class, com.ssafy.db.entity.QMeetingCompany.class, PathInits.DIRECT2);

    public final NumberPath<Integer> meetingId = createNumber("meetingId", Integer.class);

    public final ListPath<com.ssafy.db.entity.Participant, com.ssafy.db.entity.QParticipant> participants = this.<com.ssafy.db.entity.Participant, com.ssafy.db.entity.QParticipant>createList("participants", com.ssafy.db.entity.Participant.class, com.ssafy.db.entity.QParticipant.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final DateTimePath<java.util.Date> startTime = createDateTime("startTime", java.util.Date.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public final com.ssafy.db.entity.QUser user;

    public final NumberPath<Integer> userLimit = createNumber("userLimit", Integer.class);

    public QMeeting(String variable) {
        this(Meeting.class, forVariable(variable), INITS);
    }

    public QMeeting(Path<? extends Meeting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeeting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeeting(PathMetadata metadata, PathInits inits) {
        this(Meeting.class, metadata, inits);
    }

    public QMeeting(Class<? extends Meeting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.industry = inits.isInitialized("industry") ? new com.ssafy.db.entity.QIndustry(forProperty("industry")) : null;
        this.user = inits.isInitialized("user") ? new com.ssafy.db.entity.QUser(forProperty("user")) : null;
    }

}

