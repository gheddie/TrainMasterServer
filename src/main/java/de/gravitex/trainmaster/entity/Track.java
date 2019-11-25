package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
// avoid stack overflow from circular dependency (RailItemSequence <-> Track)
@EqualsAndHashCode(exclude = "railItemSequences")
public class Track extends RailtItemSequenceHolder implements PositionedItem {

	@NotNull
	@OneToOne
	private Station station;
	
	private String name;
	
	@OneToMany
	private List<RailItemSequence> railItemSequences = new ArrayList<>();
	
	public Track(String name) {
		super();
		this.name = name;
	}
	
	@Override
	@PrePersist
	public void adjustedOrdinalPositions() {
		int ordinalIndex = 0;
		for (RailItemSequence r : railItemSequences) {
			r.setOrdinalPosition(ordinalIndex);
			ordinalIndex++;
		}
	}
}