package com.maqs.rdbmsevaluation.jdbc.repository;

import com.maqs.rdbmsevaluation.jdbc.model.Movie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("MovieJdbcRepository")
public interface MovieJdbcRepository extends PagingAndSortingRepository<Movie, Long>, CustomRepository<Movie, Long> {

//    @Query("select * from movie m where m.title like :title limit :pageable.pageSize")
//    Page<Movie> findAll(String title, Pageable pageable);

}
