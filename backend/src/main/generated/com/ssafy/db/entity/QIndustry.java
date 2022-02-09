package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIndustry is a Querydsl query type for Industry
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIndustry extends EntityPathBase<Industry> {

    private static final long serialVersionUID = 139156384L;

    public static final QIndustry industry = new QIndustry("industry");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Integer> industryId = createNumber("industryId", Integer.class);

    public final StringPath industryName = createString("industryName");

    public QIndustry(String variable) {
        super(Industry.class, forVariable(variable));
    }

    public QIndustry(Path<? extends Industry> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIndustry(PathMetadata metadata) {
        super(Industry.class, metadata);
    }

}

