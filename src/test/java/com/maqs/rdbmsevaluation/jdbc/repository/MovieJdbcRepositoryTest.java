package com.maqs.rdbmsevaluation.jdbc.repository;

import com.maqs.rdbmsevaluation.it.BaseJdbcIntegrationTest;
import com.maqs.rdbmsevaluation.jdbc.model.Movie;
import com.maqs.rdbmsevaluation.jpa.repository.BatchRepositoryExecutor;
import com.maqs.rdbmsevaluation.util.EntityUtil;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@Slf4j
public class MovieJdbcRepositoryTest extends BaseJdbcIntegrationTest {

    @Autowired
    private MovieJdbcRepository repository;

    private static boolean setupDone = false;

    private long listCount = 27279;

    @Autowired
    private BatchRepositoryExecutor repositoryExecutor;

    @Autowired
    private TaskExecutor taskExecutor;

    @Before
    public  void setUpDatabase() throws Exception {
//        if (! setupDone) {
//            String file = "/data/movies.csv";
//            List<Movie> list = EntityUtil.readCsvFile(file, Movie.class, ',');
////            list = list.subList(0, 1000);
//            listCount = list.size();
//            long start = System.currentTimeMillis();
//            List<Future<String>> futures = repositoryExecutor.parallelUpsert(taskExecutor, repository, list);
//            Collection<String> messages = BatchRepositoryExecutor.getResults(futures);
////            Collection<String> messages = repositoryExecutor.upsert(movieRepository, list);
//            log.debug(messages.toString());
//            Util.printTimeTaken(start, "Inserted " + list.size() + " records");
//            setupDone = true;
//        }
    }

    @Test
    public void testMovieInsertion() {
        Movie m = new Movie();
        m.setId(5l);
        m.setTitle("Title");
        m.setGenres("Comedy");

        Movie saved = repository.insert(m);
        Long id = saved.getId();
        Assertions.assertThat(id).isNotNull();

//        m.setTitle("Title-Updated");
//        saved = repository.insert(m);

        Optional<Movie> fetched = repository.findById(saved.getId());
        Assertions.assertThat(fetched.isPresent()).isTrue();
        Assertions.assertThat(fetched.get().getTitle()).isEqualTo("Title");
    }

//    @Test
//    public void testCsvFileInsertion() {
//        long count = repository.count();
//        Assertions.assertThat(count).isGreaterThanOrEqualTo(listCount);
//    }
}
