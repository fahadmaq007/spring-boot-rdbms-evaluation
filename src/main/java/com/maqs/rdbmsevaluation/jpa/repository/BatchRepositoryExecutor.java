package com.maqs.rdbmsevaluation.jpa.repository;

import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

    public <E> List<String> upsert(JpaRepository repository, List<E> list) throws Exception {
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

    public <E> List<Future<String>> parallelUpsert(TaskExecutor taskExecutor, JpaRepository repository, List<E> list) throws Exception {
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
        private final JpaRepository repository;
        private final int batchNumber;

        BatchUpsertTask(int batchNumber, JpaRepository repository, List list){
            this.batchNumber = batchNumber;
            this.repository = repository;
            this.list = list;
        }

        @Override
        public String call() {
            String msg = Thread.currentThread().getName() + " - Batch#" + this.batchNumber + " ";
            try {
                saveAndFlush(repository, list);
            } catch (Exception e) {
                return msg + e.getMessage();
            }
            return null; //msg + "upsert task is done";
        }
    }

    private void saveAndFlush(JpaRepository repository, List list) throws Exception {
        repository.saveAll(list);
        repository.flush();
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
