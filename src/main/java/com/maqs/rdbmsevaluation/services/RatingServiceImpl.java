package com.maqs.rdbmsevaluation.services;

import com.maqs.rdbmsevaluation.exceptions.ServiceException;
import com.maqs.rdbmsevaluation.jdbc.model.Rating;
import com.maqs.rdbmsevaluation.jdbc.repository.RatingJdbcRepository;
import com.maqs.rdbmsevaluation.util.EntityUtil;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class RatingServiceImpl extends AbstractService implements RatingService {

    @Autowired
    private RatingJdbcRepository repository;

    @Autowired
    private BatchExecutor batchExecutor;

    @Autowired
    public RatingServiceImpl(RatingJdbcRepository repository) {
        this.repository = repository;
        setRepository(repository);
        repository.setParameterizedPreparedStatementSetter((ps, i) -> {
            Rating r = (Rating) i;
            ps.setLong(1, r.getUserId());
            ps.setLong(2, r.getMovieId());
            ps.setDouble(3, r.getRating());
            ps.setDate(4, new Date(r.getRatedOn().getTime()));
        });
        repository.setInsertSql(Rating.INSERT_SQL);
    }
    @Override
    public Collection<String> importCsvFileThreadExecutor(File file, char separator) throws ServiceException {
        Collection<String> result = null;
        List<Rating> list = EntityUtil.readCsvFile(file, Rating.class, separator);
        list = list.subList(0, 1000);
        try {
            List<Future<String>> futures = batchExecutor.parallelUpsert(list, batchInsertCallback);
            result = BatchExecutor.getResults(futures);
//            result = batchExecutor.upsert(ratingJdbcTemplate, list);
            log.debug("importCsvFileThreadExecutor: " + result);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Page<Rating> list(String title, String genre, Integer pageNumber, Integer pageSize) throws ServiceException {
        Pageable pageable = Util.getPageRequest(null, pageNumber, pageSize);
        Page<Rating> page = null; //repository.findAll(pageable);
        return page;
    }
}
