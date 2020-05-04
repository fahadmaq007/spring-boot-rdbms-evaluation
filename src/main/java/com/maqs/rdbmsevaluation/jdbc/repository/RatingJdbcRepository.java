package com.maqs.rdbmsevaluation.jdbc.repository;

import com.maqs.rdbmsevaluation.jdbc.model.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingJdbcRepository extends CrudRepository<Rating, Long>, CustomRepository<Rating, Long> {

//    Page<Movie> findByTitleAndGenres(String title, String genres, Pageable pageable);

}
