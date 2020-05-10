package com.maqs.rdbmsevaluation.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Movie")
@Table(name = "movie")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "title", "genres"})
public class Movie extends BaseEntity {

    public static final String[] COLUMNS = {"id", "title", "genres"};

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genres", nullable = true)
    private String genres;

    private static final CsvSchema schema = CsvSchema.builder()
            .addColumn("id")
            .addColumn("title")
            .addColumn("genres")
            .build();

    public static CsvSchema getSchema() {
        return schema;
    }
}
