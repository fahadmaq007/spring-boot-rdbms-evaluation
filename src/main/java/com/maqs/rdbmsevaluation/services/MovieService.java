package com.maqs.rdbmsevaluation.services;

import com.maqs.rdbmsevaluation.exceptions.ServiceException;
import com.maqs.rdbmsevaluation.jdbc.model.Movie;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.Collection;

public interface MovieService {

    Collection<String> importCsvFileThreadExecutor(File file, char separator) throws ServiceException;

    Page<Movie> list(String title, String genre, Integer page, Integer limit) throws ServiceException;

    long count() throws ServiceException;
}
