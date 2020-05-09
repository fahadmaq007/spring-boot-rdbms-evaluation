package com.maqs.rdbmsevaluation.jpa.repository;

import com.maqs.rdbmsevaluation.jpa.model.BaseEntity;

import java.util.List;

/**
 * @author maqbool
 */
public interface CustomRepository<T extends BaseEntity, ID> {

    List<? extends BaseEntity> insertInBatch(List<? extends BaseEntity> list);

}
