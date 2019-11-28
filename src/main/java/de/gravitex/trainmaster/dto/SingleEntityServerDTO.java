package de.gravitex.trainmaster.dto;

import de.gravitex.trainmaster.entity.BaseEntity;

public abstract class SingleEntityServerDTO<T extends BaseEntity> implements ServerDTO {

	public abstract void fillValues(T entity);
}