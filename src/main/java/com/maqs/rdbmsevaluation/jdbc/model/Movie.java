package com.maqs.rdbmsevaluation.jdbc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "movie")
@Getter
@Setter
@JsonPropertyOrder({"id", "title", "genres"})
public class Movie extends BaseEntity {
    public static final String INSERT_SQL = "insert into movie (title, genres, id) values (?,?,?)";

    public static final String UPDATE_SQL = "update movie set title = ?, genres = ? where id = ?";

    public static final String[] COLUMNS = {"id", "title", "genres"};

    @JsonProperty(value = "title")
    @Column(value = "title")
    private String title;

    @JsonProperty(value = "genres")
    @Column(value = "genres")
    private String genres;
}
