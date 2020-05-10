package com.maqs.rdbmsevaluation.jpa.repository;

import com.maqs.rdbmsevaluation.it.BaseJpaIntegrationTest;
import com.maqs.rdbmsevaluation.jpa.model.Rating;
import com.maqs.rdbmsevaluation.services.BatchExecutor;
import com.maqs.rdbmsevaluation.util.EntityUtil;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
public class RatingJpaRepositoryTest extends BaseJpaIntegrationTest {

    @Autowired
    private RatingJpaRepository repository;

    @Autowired
    private BatchExecutor batchExecutor;

    @PostConstruct
    public  void setUp() throws Exception {
        log.debug("PostContruct setup");
        setRepository(repository);
    }
    @Test
    public  void testCsvFileImport() throws Exception {
        String file = "/data/002_ratings.csv";
        List<Rating> list = EntityUtil.readCsvFile(file, Rating.class, ',');
//        list = list.subList(0, 100000);
        int listCount = list.size();
        long start = System.currentTimeMillis();
        List<Future<String>> futures = batchExecutor.parallelUpsert(list, batchCallback);
        Collection<String> messages = BatchExecutor.getResults(futures);
//            Collection<String> messages = batchExecutor.upsert(movieRepository, list);
        log.debug(messages.toString());
        Util.printTimeTaken(start, "Inserted " + list.size() + " records");

        long count = repository.count();
        Assertions.assertThat(count).isEqualTo(listCount);
    }
}
