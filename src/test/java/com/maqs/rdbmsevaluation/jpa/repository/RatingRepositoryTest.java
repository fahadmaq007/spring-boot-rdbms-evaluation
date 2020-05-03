package com.maqs.rdbmsevaluation.jpa.repository;

import com.maqs.rdbmsevaluation.it.BaseIntegrationTest;
import com.maqs.rdbmsevaluation.jpa.model.Movie;
import com.maqs.rdbmsevaluation.jpa.model.Rating;
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
import java.util.Optional;
import java.util.concurrent.Future;

@Slf4j
public class RatingRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private RatingRepository ratingRepository;

    private static boolean setupDone = false;

    private long listCount = 27279;

    @Autowired
    private BatchRepositoryExecutor repositoryExecutor;

    @Autowired
    private TaskExecutor taskExecutor;

    @Before
    public  void setUpDatabase() throws Exception {
        if (! setupDone) {
            String file = "/data/001_ratings.csv";
            List<Rating> list = EntityUtil.readCsvFile(file, Rating.class, ',');
//            list = list.subList(0, 1000);
            listCount = list.size();
            long start = System.currentTimeMillis();
            List<Future<String>> futures = repositoryExecutor.parallelUpsert(taskExecutor, ratingRepository, list);
            Collection<String> messages = BatchRepositoryExecutor.getResults(futures);
//            Collection<String> messages = repositoryExecutor.upsert(movieRepository, list);
            log.debug(messages.toString());
            Util.printTimeTaken(start, "Inserted " + list.size() + " records");
            setupDone = true;
        }
    }

    @Test
    public void testRatingListById() {
        long count = ratingRepository.count();
        Assertions.assertThat(count).isGreaterThanOrEqualTo(listCount);
    }
}
