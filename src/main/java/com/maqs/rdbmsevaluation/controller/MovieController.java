package com.maqs.rdbmsevaluation.controller;

import com.maqs.rdbmsevaluation.exceptions.ServiceException;
import com.maqs.rdbmsevaluation.jpa.model.Movie;
import com.maqs.rdbmsevaluation.services.MovieService;
import com.maqs.rdbmsevaluation.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

    @Autowired
    private MovieService movieService;

    @RequestMapping(value = "/importcsv", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Collection<String> importCsv(@RequestParam(value = "file", required = true) MultipartFile file)
            throws ServiceException {
        if (file == null) {
            throw new IllegalArgumentException("file not found");
        }
        File f = Util.getFile(file);
        return movieService.importCsvFileThreadExecutor(f, ',');
    }

    @RequestMapping(value = "" , method = RequestMethod.GET)
    public Page<Movie> list(@RequestParam(value="title", required = false) String title,
                             @RequestParam(value="genre", required = false) String genre,
                             @RequestParam(value="page", required = false) Integer page,
                             @RequestParam(value="limit", required = false) Integer limit) throws ServiceException {
        return movieService.list(title, genre, page, limit);
    }
}
