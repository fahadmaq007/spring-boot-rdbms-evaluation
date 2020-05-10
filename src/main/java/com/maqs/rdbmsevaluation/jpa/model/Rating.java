package com.maqs.rdbmsevaluation.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.maqs.rdbmsevaluation.json.UnixTimestampDeserializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Rating")
@Table(name = "rating")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"userId", "movieId", "rating", "ratedOn"})
public class Rating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "user_id", nullable = true)
    @JsonProperty(required=true)
    private Long userId;

    @Column(name = "movie_id", nullable = true)
    @JsonProperty(required=true)
    private Long movieId;

    @Column(name = "rating", nullable = true)
    @JsonProperty(required=true)
    private Double rating;

    @Column(name = "rated_on", nullable = true)
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private Date ratedOn;
}
