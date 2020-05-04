package com.maqs.rdbmsevaluation.it.services;

import com.maqs.rdbmsevaluation.exceptions.ServiceException;
import com.maqs.rdbmsevaluation.it.BaseJpaIntegrationTest;
import com.maqs.rdbmsevaluation.services.MovieService;
import com.maqs.rdbmsevaluation.services.MovieServiceImpl;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

@Slf4j
@ContextConfiguration(classes = { MovieServiceImpl.class})
public class MovieServiceTest extends BaseJpaIntegrationTest {

    @Autowired
    private MovieService movieService;

    @Test
    public void testImportCsvFileThreadExecutor() throws ServiceException {
        String filePath = "/data/movies.csv";
        File file = Util.getClasspathFile(filePath);
        movieService.importCsvFileThreadExecutor(file, ',');
    }
}
