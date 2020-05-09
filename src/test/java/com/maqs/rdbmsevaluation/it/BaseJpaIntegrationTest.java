package com.maqs.rdbmsevaluation.it;

import com.maqs.rdbmsevaluation.BaseTest;
import com.maqs.rdbmsevaluation.config.TaskPoolConfiguration;
import com.maqs.rdbmsevaluation.jpa.repository.CustomRepository;
import com.maqs.rdbmsevaluation.services.BatchExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@DataJpaTest()
@EnableJpaRepositories(basePackages = {"com.maqs.rdbmsevaluation.jpa.repository"})
@EntityScan(basePackages = "com.maqs.rdbmsevaluation.jpa.model")
@ContextConfiguration(classes = { TaskPoolConfiguration.class, BatchExecutor.class})
@TestPropertySource(locations = { "classpath:application-default.properties" })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
@Slf4j
public abstract class BaseJpaIntegrationTest extends BaseTest {

    protected CustomRepository repository;

    protected void setRepository(CustomRepository repository) {
        this.repository = repository;
    }

    protected Consumer<List> batchCallback = (batchList) -> {
        log.debug("batchCallack is called on " + repository);
        repository.insertInBatch(batchList);
    };

}
