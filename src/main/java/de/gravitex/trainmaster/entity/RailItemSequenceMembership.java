package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class RailItemSequenceMembership extends BaseEntity {

	public RailItemSequenceMembership(RailItem railItem) {
		super();
		this.railItem = railItem;
	}

	@OneToOne
	private RailItem railItem;
	
	@OneToOne
	private RailtItemSequence railtItemSequence;
	
	private int ordinalPosition;
	
	public String toString() {
		return railItem.getIdentifier() + "@" + ordinalPosition;
	}
}