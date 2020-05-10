package com.maqs.rdbmsevaluation.jdbc.repository;

/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.maqs.rdbmsevaluation.jdbc.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Jens Schauder
 */
@Slf4j
public class CustomRepositoryImpl<T extends BaseEntity, ID> implements CustomRepository<T, ID> {
    @Autowired
    JdbcAggregateTemplate jdbcAggregateTemplate;

    @Autowired
    NamedParameterJdbcOperations namedParameterJdbcOperations;

    private ParameterizedPreparedStatementSetter parameterizedPreparedStatementSetter;

    private String insertSql;

    private String updateSql;

    @Override
    public BaseEntity upsert(BaseEntity t) {
        Long id = t.getId();
        if (id != null) {
            boolean exists = jdbcAggregateTemplate.existsById(id, t.getClass());
            t.setNew(! exists);
        }
        return jdbcAggregateTemplate.save(t);
    }

    @Override
    public int[] insertInBatch(List<? extends BaseEntity> list, int batchSize) {
        Assert.notNull(parameterizedPreparedStatementSetter,
                "ParameterizedPreparedStatementSetter must not be null");
        Assert.notNull(insertSql, "insertSql must not be null");
        namedParameterJdbcOperations.getJdbcOperations().batchUpdate(insertSql, list,
                batchSize, parameterizedPreparedStatementSetter);
        return null;
    }

    @Override
    public void setParameterizedPreparedStatementSetter(ParameterizedPreparedStatementSetter ps) {
        this.parameterizedPreparedStatementSetter = ps;
    }

    @Override
    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }
}
