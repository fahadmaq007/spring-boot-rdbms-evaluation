package com.maqs.rdbmsevaluation.it.services;

import com.maqs.rdbmsevaluation.exceptions.ServiceException;
import com.maqs.rdbmsevaluation.it.BaseJpaIntegrationTest;
import com.maqs.rdbmsevaluation.services.RatingService;
import com.maqs.rdbmsevaluation.services.RatingServiceImpl;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

@Slf4j
@ContextConfiguration(classes = { RatingServiceImpl.class })
public class RatingServiceTest extends BaseJpaIntegrationTest {

    @Autowired
    private RatingService ratingService;

    @Test
    public void testImportCsvFileThreadExecutor() throws ServiceException {
        String filePath = "/data/001_ratings.csv";
        File file = Util.getClasspathFile(filePath);
        ratingService.importCsvFileThreadExecutor(file, ',');
    }
}
