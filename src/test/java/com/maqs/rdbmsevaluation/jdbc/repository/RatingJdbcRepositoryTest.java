package com.maqs.rdbmsevaluation.jdbc.repository;

import com.maqs.rdbmsevaluation.it.BaseJdbcIntegrationTest;
import com.maqs.rdbmsevaluation.jdbc.model.Rating;
import com.maqs.rdbmsevaluation.services.BatchExecutor;
import com.maqs.rdbmsevaluation.util.EntityUtil;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
public class RatingJdbcRepositoryTest extends BaseJdbcIntegrationTest {

    @Autowired
    private RatingJdbcRepository repository;

    @Autowired
    private BatchExecutor repositoryExecutor;

    @PostConstruct
    public  void setUp() throws Exception {
        log.debug("PostContruct setup");
        setRepository(repository);
        repository.setParameterizedPreparedStatementSetter((ps, i) -> {
            Rating r = (Rating) i;
            ps.setLong(1, r.getUserId());
            ps.setLong(2, r.getMovieId());
            ps.setDouble(3, r.getRating());
            ps.setDate(4, new Date(r.getRatedOn().getTime()));
        });
        repository.setInsertSql(Rating.INSERT_SQL);
    }
        @Test
    public  void testCsvFileImport() throws Exception {
        String file = "/data/002_ratings.csv";
        List<Rating> list = EntityUtil.readCsvFile(file, Rating.class, ',');
//        list = list.subList(0, 1000);
        int listCount = list.size();
        long start = System.currentTimeMillis();
        List<Future<String>> futures = repositoryExecutor.parallelUpsert(list, batchInsertCallback);
        Collection<String> messages = BatchExecutor.getResults(futures);
//      Collection<String> messages = repositoryExecutor.upsert(repository, list);
        log.debug(messages.toString());
        Util.printTimeTaken(start, "Inserted " + list.size() + " records");
        long count = repository.count();
        Assertions.assertThat(count).isEqualTo(listCount);
    }

    @Test
    public void testRatingInsertion() {

    }
}
