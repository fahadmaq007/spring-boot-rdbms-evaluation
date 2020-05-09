package com.maqs.rdbmsevaluation.jpa.repository;

import com.maqs.rdbmsevaluation.it.BaseJpaIntegrationTest;
import com.maqs.rdbmsevaluation.jpa.model.Movie;
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
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@Slf4j
public class MovieJpaRepositoryTest extends BaseJpaIntegrationTest {

    @Autowired
    private MovieJpaRepository repository;

    @Autowired
    private BatchExecutor repositoryExecutor;

    @PostConstruct
    public  void setUp() throws Exception {
        log.debug("PostContruct setup");
        setRepository(repository);
    }

    @Test
    public  void testCsvFileImport() throws Exception {
        String file = "/data/movies.csv";
        List<Movie> list = EntityUtil.readCsvFile(file, Movie.class, ',');
            list = list.subList(0, 1000);
        int listCount = list.size();
        long start = System.currentTimeMillis();

        List<Future<String>> futures = repositoryExecutor.parallelUpsert(list, batchCallback);
        Collection<String> messages = BatchExecutor.getResults(futures);
//            Collection<String> messages = repositoryExecutor.upsert(movieRepository, list);
        log.debug(messages.toString());
        Util.printTimeTaken(start, "Inserted " + list.size() + " records");

        long count = repository.count();
        Assertions.assertThat(count).isEqualTo(listCount);
    }

    @Test
    public void testMovieInsertion() {
        Movie m = new Movie();
        m.setId(-1l);
        m.setTitle("Title");
        m.setGenres("Comedy");

        Movie saved = repository.save(m);
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
        Page<Movie> page = repository.findAll(p);
        log.debug("Page is " + page.getNumber() + " " + page.getTotalElements() + " " + page.getContent() );
        Assertions.assertThat(page).isNotNull();
    }
}
