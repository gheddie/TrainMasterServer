package de.gravitex.trainmaster.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import de.gravitex.trainmaster.entity.check.RailItemCheck;
import lombok.Data;

@Entity
@Data
public abstract class RailItem extends BaseEntity {
	
	public abstract String getIdentifier();

	public abstract RailItem asConcreteItem();
	
	@OneToMany
	private List<RailItemCheck<RailItem>> railItemChecks;
}