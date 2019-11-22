package de.gravitex.trainmaster.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Id
    private long id;
}