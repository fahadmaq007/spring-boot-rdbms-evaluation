package com.maqs.rdbmsevaluation.jpa.repository;

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

import com.maqs.rdbmsevaluation.jpa.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * @author maqbool
 */
@Slf4j
public class CustomRepositoryImpl<T extends BaseEntity, ID> implements CustomRepository<T, ID> {

    @Autowired
    EntityManagerFactory emf;

    @Override
    public List<? extends BaseEntity> insertInBatch(List<? extends BaseEntity> list) {
        Assert.notNull(emf, "emf cannot be null");
        Assert.notNull(list, "list cannot be null");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        list.forEach(item -> em.persist(item));
        em.getTransaction().commit();
        em.close();
        log.debug("JPA insertInBatch committed " + list.size() + " items");
        return list;
    }
}
