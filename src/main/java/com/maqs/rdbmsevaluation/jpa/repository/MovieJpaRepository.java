package com.maqs.rdbmsevaluation.jpa.repository;

import com.maqs.rdbmsevaluation.jpa.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieJpaRepository extends JpaRepository<Movie, Long>, CustomRepository {

    Page<Movie> findByTitleAndGenres(String title, String genres, Pageable pageable);
}
