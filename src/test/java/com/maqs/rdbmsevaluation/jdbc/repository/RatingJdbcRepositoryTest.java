package com.maqs.rdbmsevaluation.jdbc.repository;

import com.maqs.rdbmsevaluation.it.BaseJdbcIntegrationTest;
import com.maqs.rdbmsevaluation.jdbc.model.Movie;
import com.maqs.rdbmsevaluation.jdbc.model.Rating;
import com.maqs.rdbmsevaluation.jpa.repository.BatchRepositoryExecutor;
import com.maqs.rdbmsevaluation.jpa.repository.RatingJpaRepository;
import com.maqs.rdbmsevaluation.util.EntityUtil;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
public class RatingJdbcRepositoryTest extends BaseJdbcIntegrationTest {

    @Autowired
    private RatingJdbcRepository repository;

    private static boolean setupDone = false;

    private long listCount = 27279;

    @Autowired
    private BatchRepositoryExecutor repositoryExecutor;

    @Autowired
    private TaskExecutor taskExecutor;

    @Before
    public  void setUpDatabase() throws Exception {
        if (! setupDone) {

            String file = "/data/002_ratings.csv";
            List<Rating> list = EntityUtil.readCsvFile(file, Rating.class, ',');
//            list = list.subList(0, 1000);
            listCount = list.size();
            long start = System.currentTimeMillis();
            List<Future<String>> futures = repositoryExecutor.parallelUpsert(taskExecutor, repository, list);
            Collection<String> messages = BatchRepositoryExecutor.getResults(futures);
//            Collection<String> messages = repositoryExecutor.upsert(movieRepository, list);
            log.debug(messages.toString());
            Util.printTimeTaken(start, "Inserted " + list.size() + " records");
            setupDone = true;
        }
    }

    @Test
    public void testMovieInsertion() {
//        Movie m = new Movie();
////        m.setId(1l);
//        m.setTitle("Title");
//        m.setGenres("Comedy");
//
//        Movie saved = repository.save(m);
//        Assertions.assertThat(saved.getId()).isNotNull();
        long count = repository.count();
        Assertions.assertThat(count).isEqualTo(listCount);
    }
}
