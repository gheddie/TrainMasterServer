package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public abstract class RailItem extends BaseEntity {
	
	public abstract String getIdentifier();

	public abstract RailItem asConcreteItem();
}