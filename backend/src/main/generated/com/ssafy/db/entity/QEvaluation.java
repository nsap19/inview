package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QEvaluation is a Querydsl query type for Evaluation
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEvaluation extends EntityPathBase<Evaluation> {

    private static final long serialVersionUID = -494684034L;

    public static final QEvaluation evaluation = new QEvaluation("evaluation");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> evaluationId = createNumber("evaluationId", Integer.class);

    public QEvaluation(String variable) {
        super(Evaluation.class, forVariable(variable));
    }

    public QEvaluation(Path<? extends Evaluation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEvaluation(PathMetadata metadata) {
        super(Evaluation.class, metadata);
    }

}

