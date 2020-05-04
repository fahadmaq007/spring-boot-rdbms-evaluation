package com.maqs.rdbmsevaluation.services;

import com.maqs.rdbmsevaluation.exceptions.ServiceException;
import com.maqs.rdbmsevaluation.jpa.model.Movie;
import com.maqs.rdbmsevaluation.jpa.model.Rating;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.Collection;

public interface RatingService {

    Collection<String> importCsvFileThreadExecutor(File file, char separator) throws ServiceException;

    Page<Rating> list(String title, String genre, Integer page, Integer limit) throws ServiceException;
}
