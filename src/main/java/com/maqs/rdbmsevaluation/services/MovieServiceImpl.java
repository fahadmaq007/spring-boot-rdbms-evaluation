package com.maqs.rdbmsevaluation.services;

import com.maqs.rdbmsevaluation.exceptions.ServiceException;
import com.maqs.rdbmsevaluation.jdbc.repository.MovieJdbcRepository;
import com.maqs.rdbmsevaluation.jpa.model.Movie;
import com.maqs.rdbmsevaluation.jpa.repository.BatchRepositoryExecutor;
import com.maqs.rdbmsevaluation.util.EntityUtil;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class MovieServiceImpl extends AbstractService implements MovieService {

    @Autowired
    private MovieJdbcRepository repository;

    @Autowired
    private BatchRepositoryExecutor batchExecutor;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public Collection<String> importCsvFileThreadExecutor(File file, char separator) throws ServiceException {
        Collection<String> result = null;
        List<Movie> list = EntityUtil.readCsvFile(file, Movie.class, separator);
        try {
            List<Future<String>> futures = batchExecutor.parallelUpsert(taskExecutor, repository, list);
            result = BatchRepositoryExecutor.getResults(futures);
            log.debug("importCsvFileThreadExecutor: " + result);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Page<Movie> list(String title, String genre, Integer pageNumber, Integer pageSize) throws ServiceException {
        Pageable pageable = Util.getPageRequest(null, pageNumber, pageSize);
        Page<Movie> page = null;//repository.findByTitleAndGenres(title, genre, pageable);
        return page;
    }
}
