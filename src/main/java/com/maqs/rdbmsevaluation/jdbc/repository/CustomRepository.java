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

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.util.List;

/**
 * @author Jens Schauder
 */
public interface CustomRepository<T, ID> {
    T insert(T t);

    List<T> insert(List<T> list, int batchSize);

    int[] batchUpdate(String sql, List<T> list);
}
