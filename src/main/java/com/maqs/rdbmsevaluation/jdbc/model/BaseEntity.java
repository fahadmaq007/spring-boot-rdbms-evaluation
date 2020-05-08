package com.maqs.rdbmsevaluation.jdbc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity implements Persistable {
    @Id
    @JsonProperty(value = "id")
    @Column(value = "id")
    protected Long id;

    public static Set<Long> getIds(List<? extends BaseEntity> list) {
        Set<Long> ids = list.stream().map(BaseEntity::getId).collect(Collectors.toSet());
        return ids;
    }

    @Transient
    protected boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
