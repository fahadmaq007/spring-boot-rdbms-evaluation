package com.maqs.rdbmsevaluation.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity implements Persistable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

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
