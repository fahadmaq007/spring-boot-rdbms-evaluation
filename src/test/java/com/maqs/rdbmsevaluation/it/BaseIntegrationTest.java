package com.maqs.rdbmsevaluation.it;

import com.maqs.rdbmsevaluation.BaseTest;
import com.maqs.rdbmsevaluation.config.TaskPoolConfiguration;
import com.maqs.rdbmsevaluation.jpa.repository.BatchRepositoryExecutor;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest()
@EnableJpaRepositories(basePackages = "com.maqs.rdbmsevaluation.jpa.repository")
@EntityScan(basePackages = "com.maqs.rdbmsevaluation.jpa.model")
@ContextConfiguration(classes = { TaskPoolConfiguration.class, BatchRepositoryExecutor.class })
@TestPropertySource(locations = { "classpath:application-default.properties" })
@AutoConfigureTestDatabase(replace= Replace.NONE)
@Rollback(false)
public abstract class BaseIntegrationTest extends BaseTest {

    public BaseIntegrationTest() { }

}
