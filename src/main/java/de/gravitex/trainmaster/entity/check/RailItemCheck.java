package de.gravitex.trainmaster.entity.check;

import javax.persistence.Entity;

import de.gravitex.trainmaster.entity.BaseEntity;
import de.gravitex.trainmaster.entity.RailItem;
import lombok.Data;

@Entity
@Data
public abstract class RailItemCheck<T extends RailItem> extends BaseEntity {

}