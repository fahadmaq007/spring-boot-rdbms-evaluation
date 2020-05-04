package com.maqs.rdbmsevaluation.jdbc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "movie")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "title", "genres"})
public class Movie {

    public static final String[] COLUMNS = {"id", "title", "genres"};

    @Id
    @JsonProperty(value = "noId")
    private Long id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "genres")
    private String genres;

}
