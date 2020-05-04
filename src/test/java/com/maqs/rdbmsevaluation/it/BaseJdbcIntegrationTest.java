package com.maqs.rdbmsevaluation.it;

import com.maqs.rdbmsevaluation.BaseTest;
import com.maqs.rdbmsevaluation.RdbmsEvaluationApplication;
import com.maqs.rdbmsevaluation.config.TaskPoolConfiguration;
import com.maqs.rdbmsevaluation.jdbc.repository.MovieJdbcRepository;
import com.maqs.rdbmsevaluation.jpa.repository.BatchRepositoryExecutor;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJdbcTest()
//@SpringBootTest(classes = RdbmsEvaluationApplication.class)
//@EnableJdbcRepositories(basePackageClasses = {MovieJdbcRepository.class})
@EnableJdbcRepositories(basePackages = {"com.maqs.rdbmsevaluation.jdbc.repository"})
@ContextConfiguration(classes = { TaskPoolConfiguration.class, BatchRepositoryExecutor.class})
@TestPropertySource(locations = { "classpath:application-default.properties" })
@AutoConfigureTestDatabase(replace= Replace.NONE)

@Rollback(false)
public abstract class BaseJdbcIntegrationTest extends BaseTest {

    public BaseJdbcIntegrationTest() { }

}
