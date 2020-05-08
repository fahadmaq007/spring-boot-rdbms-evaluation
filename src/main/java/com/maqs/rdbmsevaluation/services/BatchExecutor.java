package com.maqs.rdbmsevaluation.services;

import com.maqs.rdbmsevaluation.jdbc.repository.CustomRepository;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BatchExecutor {

    private static  final int BATCH_SIZE = 100;

    @Autowired
    private TaskExecutor taskExecutor;

    public <E> List<String> upsert(List<E> list, Consumer<List> batchCallback) throws Exception {
        long start = System.currentTimeMillis();
        List<String> messages = new ArrayList<>();
        Collection<List<E>> batches = Util.splitInBatches(list, BATCH_SIZE);
        AtomicInteger i = new AtomicInteger(1);
        for (List batchList: batches) {
            String msg = Thread.currentThread().getName() + " - Batch#" + i.getAndIncrement() + " ";
            try {
                batchCallback.accept(batchList);
                msg = msg + "upsert task is done";
            } catch (Exception e) {
                msg = msg + e.getMessage();
            }
            messages.add(msg);
        }

        Util.printTimeTaken(start, "Inserted " + list.size() + " records");
        return messages;
    }

    public <E> List<Future<String>> parallelUpsert(List<E> list, Consumer<List> batchCallback) throws Exception {
        CompletionService<List> completionService = new ExecutorCompletionService<>(taskExecutor);
        List<Future<String>> futures = new ArrayList<>();
        final AtomicInteger counter = new AtomicInteger(1);
        Collection<List<E>> batches = Util.splitInBatches(list, BATCH_SIZE);
        batches.forEach(batchList -> {
            int batchNum = counter.getAndIncrement();
            BatchUpsertTask batchUpsertTask = new BatchUpsertTask(batchNum, batchCallback, batchList);
            Future<String> future = completionService.submit(batchUpsertTask);
            futures.add(future);
        });
        return futures;
    }

    private final class BatchUpsertTask implements Callable {
        private final List batchList;
        private Consumer<List> batchCallback;
        private final int batchNumber;

        BatchUpsertTask(int batchNumber, Consumer<List> batchCallback, List batchList){
            this.batchNumber = batchNumber;
            this.batchCallback = batchCallback;
            this.batchList = batchList;
        }

        @Override
        public String call() {
            String msg = Thread.currentThread().getName() + " - Batch#" + this.batchNumber + " ";
            try {
                batchCallback.accept(batchList);
            } catch (Exception e) {
                log.error(msg + " " + e.getMessage(), e);
            }
            return null; //msg + "upsert task is done";
        }
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
}
