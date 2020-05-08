package com.maqs.rdbmsevaluation.jdbc.repository;

import com.maqs.rdbmsevaluation.jdbc.model.BaseEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import java.util.List;

/**
 * @author maqbool
 */
public interface CustomRepository<T extends BaseEntity, ID> {

    BaseEntity upsert(BaseEntity t);

    int[] insertInBatch(List<? extends BaseEntity> list, int batchSize);

    void setParameterizedPreparedStatementSetter(ParameterizedPreparedStatementSetter ps);

    void setInsertSql(String sql);
}
