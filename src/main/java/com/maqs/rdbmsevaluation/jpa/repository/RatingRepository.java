package com.maqs.rdbmsevaluation.jpa.repository;

import com.maqs.rdbmsevaluation.jpa.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

}
