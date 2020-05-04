package com.maqs.rdbmsevaluation.jpa.repository;

import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BatchRepositoryExecutor {

    private static  final int BATCH_SIZE = 1000;

//    public <E> List<String> upsert(EntityJdbcTemplate<E> jdbcTemplate , List<E> list) throws Exception {
//        long start = System.currentTimeMillis();
//        List<String> messages = new ArrayList<>();
//        Collection<List<E>> batches = Util.splitInBatches(list, BATCH_SIZE);
//        AtomicInteger i = new AtomicInteger(1);
//        for (List batchList: batches) {
//            String msg = Thread.currentThread().getName() + " - Batch#" + i.getAndIncrement() + " ";
//            try {
//                jdbcTemplate.batchInsert(batchList, 50);
//                msg = msg + "upsert task is done";
//            } catch (Exception e) {
//                msg = msg + e.getMessage();
//            }
//            messages.add(msg);
//        }
//
//        Util.printTimeTaken(start, "Inserted " + list.size() + " records");
//        return messages;
//    }

    public <E> List<String> upsert(CrudRepository repository, List<E> list) throws Exception {
        long start = System.currentTimeMillis();
        List<String> messages = new ArrayList<>();
        Collection<List<E>> batches = Util.splitInBatches(list, BATCH_SIZE);
        AtomicInteger i = new AtomicInteger(1);
        for (List batchList: batches) {
            String msg = Thread.currentThread().getName() + " - Batch#" + i.getAndIncrement() + " ";
            try {
                saveAndFlush(repository, batchList);
                msg = msg + "upsert task is done";
            } catch (Exception e) {
                msg = msg + e.getMessage();
            }
            messages.add(msg);
        }

        Util.printTimeTaken(start, "Inserted " + list.size() + " records");
        return messages;
    }

    public <E> List<Future<String>> parallelUpsert(TaskExecutor taskExecutor, CrudRepository repository, List<E> list) throws Exception {
        CompletionService<List> completionService = new ExecutorCompletionService<>(taskExecutor);
        List<Future<String>> futures = new ArrayList<>();
        final AtomicInteger counter = new AtomicInteger(1);
        Collection<List<E>> batches = Util.splitInBatches(list, BATCH_SIZE);
        batches.forEach(batchList -> {
            BatchUpsertTask batchUpsertTask = new BatchUpsertTask(counter.getAndIncrement(), repository, batchList);
            Future<String> future = completionService.submit(batchUpsertTask);
            futures.add(future);
        });
        return futures;
    }

    private final class BatchUpsertTask implements Callable {
        private final List list;
        private final CrudRepository repository;
//        private EntityJdbcTemplate jdbcTemplate;
        private final int batchNumber;

        BatchUpsertTask(int batchNumber, CrudRepository repository, List list){
            this.batchNumber = batchNumber;
            this.repository = repository;
            this.list = list;
        }

//        BatchUpsertTask(int batchNumber, EntityJdbcTemplate jdbcTemplate, List list){
//            this.batchNumber = batchNumber;
//            this.jdbcTemplate = jdbcTemplate;
//            this.list = list;
//        }

        @Override
        public String call() {
            String msg = Thread.currentThread().getName() + " - Batch#" + this.batchNumber + " ";
            try {
//                if (jdbcTemplate != null) {
//                    jdbcTemplate.batchInsert(list, 50);
//                } else if (repository != null) {
//                    saveAndFlush(repository, list);
//                }
                saveAndFlush(repository, list);
            } catch (Exception e) {
                return msg + e.getMessage();
            }
            return null; //msg + "upsert task is done";
        }
    }

    private void saveAndFlush(CrudRepository repository, List list) throws Exception {
        for (Object e: list) {
            repository.save(e);
        }
//        repository.saveAll(list);
//        repository.flush();
    }

    public static Collection<String> getResults(List<Future<String>> futures) {
        long start = System.currentTimeMillis();
        Collection<String> c = futures.stream().map(m -> {
            try {
                return m.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        Util.printTimeTaken(start, "Future results");
        return c;
    }

//    public <E> List<Future<String>> parallelUpsert(TaskExecutor taskExecutor, EntityJdbcTemplate jdbcTemplate, List<E> list) throws Exception {
//        CompletionService<List> completionService = new ExecutorCompletionService<>(taskExecutor);
//        List<Future<String>> futures = new ArrayList<>();
//        final AtomicInteger counter = new AtomicInteger(1);
//        Collection<List<E>> batches = Util.splitInBatches(list, BATCH_SIZE);
//        batches.forEach(batchList -> {
//            BatchUpsertTask batchUpsertTask = new BatchUpsertTask(counter.getAndIncrement(), jdbcTemplate, batchList);
//            Future<String> future = completionService.submit(batchUpsertTask);
//            futures.add(future);
//        });
//        return futures;
//    }
}
