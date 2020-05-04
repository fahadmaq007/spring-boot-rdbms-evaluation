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

import com.maqs.rdbmsevaluation.jdbc.model.Rating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.Date;
import java.util.List;

/**
 * @author Jens Schauder
 */
@Slf4j
public class CustomRepositoryImpl<T, ID> implements CustomRepository<T, ID> {
    @Autowired
    JdbcAggregateTemplate template;

    @Autowired
    NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public T insert(T t) {
        return template.insert(t);
    }

    @Override
    public List<T> insert(List<T> list, int batchSize) {
        return null; //template.;
    }

    @Override
    public int[] batchUpdate(String sql, List<T> list) {
        namedParameterJdbcOperations.getJdbcOperations().batchUpdate(sql, list, 50, (ps, i) -> {
            Rating r = (Rating) i;
            ps.setLong(1, r.getUserId());
            ps.setLong(2, r.getMovieId());
            ps.setDouble(3, r.getRating());
            ps.setDate(4, new Date(r.getRatedOn().getTime()));
        });
        return null;
    }
}
