package com.maqs.rdbmsevaluation.it;

import com.maqs.rdbmsevaluation.BaseTest;
import com.maqs.rdbmsevaluation.config.TaskPoolConfiguration;
import com.maqs.rdbmsevaluation.jdbc.repository.CustomRepository;
import com.maqs.rdbmsevaluation.services.BatchExecutor;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@DataJdbcTest
@EnableJdbcRepositories(basePackages = {"com.maqs.rdbmsevaluation.jdbc.repository"})
@ContextConfiguration(classes = { TaskPoolConfiguration.class, BatchExecutor.class})
@TestPropertySource(locations = { "classpath:application-default.properties" })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public abstract class BaseJdbcIntegrationTest extends BaseTest {
    protected CustomRepository repository;

    protected void setRepository(CustomRepository repository) {
        this.repository = repository;
    }

    protected Consumer<List> batchInsertCallback = (batchList) -> {
        repository.insertInBatch(batchList, 100);
    };
}
