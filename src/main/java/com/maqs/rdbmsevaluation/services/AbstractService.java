package com.maqs.rdbmsevaluation.services;

import com.maqs.rdbmsevaluation.jdbc.repository.CustomRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractService {
    protected CustomRepository repository;

    protected void setRepository(CustomRepository repository) {
        this.repository = repository;
    }

    protected Consumer<List> batchInsertCallback = (batchList) -> {
        repository.insertInBatch(batchList, 100);
    };
}
