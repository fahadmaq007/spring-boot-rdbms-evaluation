package com.maqs.rdbmsevaluation.jdbc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table(value = "rating")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"userId", "movieId", "rating", "ratedOn"})
public class Rating {

    @Id
    private Long id;

    @JsonProperty(value = "userId")
    private Long userId;

    @JsonProperty(value = "movieId")
    private Long movieId;

    @JsonProperty(value = "rating")
    private Double rating;

    @JsonProperty(value = "ratedOn")
    private Date ratedOn;
}
