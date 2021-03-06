package com.maqs.rdbmsevaluation.jdbc.repository;

import com.maqs.rdbmsevaluation.it.BaseJdbcIntegrationTest;
import com.maqs.rdbmsevaluation.jdbc.model.BaseEntity;
import com.maqs.rdbmsevaluation.jdbc.model.Movie;
import com.maqs.rdbmsevaluation.services.BatchExecutor;
import com.maqs.rdbmsevaluation.util.EntityUtil;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Future;

@Slf4j
public class MovieJdbcRepositoryTest extends BaseJdbcIntegrationTest {

    @Autowired
    private MovieJdbcRepository repository;

    private long listCount = 27279;

    @Autowired
    private BatchExecutor repositoryExecutor;

    @PostConstruct
    public  void setUp() throws Exception {
        log.debug("PostContruct setup");
        setRepository(repository);
        repository.setParameterizedPreparedStatementSetter((ps, i) -> {
            Movie r = (Movie) i;
            ps.setString(1, r.getTitle());
            ps.setString(2, r.getGenres());
            ps.setLong(3, r.getId());
        });
        repository.setInsertSql(Movie.INSERT_SQL);
    }

    @Test
    public  void testCsvFileImport() throws Exception {
        String file = "/data/movies.csv";
        List<Movie> list = EntityUtil.readCsvFile(file, Movie.class, ',');
            list = list.subList(0, 1000);
        listCount = list.size();
        long start = System.currentTimeMillis();

        List<Future<String>> futures = repositoryExecutor.parallelUpsert(list, batchInsertCallback);
        Collection<String> messages = BatchExecutor.getResults(futures);
//            Collection<String> messages = repositoryExecutor.upsert(repository, list);
        log.debug(messages.toString());
        Util.printTimeTaken(start, "Inserted " + list.size() + " records");
        long count = repository.count();
        Assertions.assertThat(count).isEqualTo(listCount);
    }

    @Test
    public void testMovieInsertion() {
        Movie m = new Movie();
        m.setId(6l);
        m.setTitle("Title");
        m.setGenres("Comedy");

        BaseEntity saved = repository.upsert(m);

        repository.saveAll(new ArrayList<>());
        Long id = saved.getId();
        Assertions.assertThat(id).isNotNull();

//        m.setTitle("Title-Updated");
//        saved = repository.insert(m);

        Optional<Movie> fetched = repository.findById(saved.getId());
        Assertions.assertThat(fetched.isPresent()).isTrue();
        Assertions.assertThat(fetched.get().getTitle()).isEqualTo("Title");
    }

    @Test
    public void testListMoviesByTitle() {
        Pageable p = Util.getPageRequest(null, 1, 20);
//        Page<Movie> page = repository.findAll(p);
        Iterable<Movie> page = repository.findAll();
//        log.debug("Page is " + page.getNumber() + " " + page.getTotalElements() + " " + page.getContent() );
        Assertions.assertThat(page).isNotNull();
    }
}
