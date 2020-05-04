package com.maqs.rdbmsevaluation.jdbc.repository;

import com.maqs.rdbmsevaluation.jdbc.model.Movie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("MovieJdbcRepository")
public interface MovieJdbcRepository extends CrudRepository<Movie, Long>, CustomRepository<Movie, Long> {

//    Page<Movie> findByTitleAndGenres(String title, String genres, Pageable pageable);

}
