package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
// avoid stack overflow from circular dependency (RailItemSequence <-> Track)
@EqualsAndHashCode(exclude = "railItemSequences")
public class Track extends RailItemSequenceHolder implements PositionedItem {

	@NotNull
	@OneToOne
	private Station station;
	
	@NotBlank
	private String trackNumber;
	
	@OneToMany
	private List<RailItemSequence> railItemSequences = new ArrayList<>();
	
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