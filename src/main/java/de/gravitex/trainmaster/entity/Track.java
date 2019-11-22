package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
// avoid stack overflow from circular dependency (RailItemSequence <-> Track)
@EqualsAndHashCode(exclude = "railItemSequences")
public class Track extends RailtItemSequenceHolder {

	@OneToOne
	private Station station;
	
	private String name;
	
	@OneToMany
	private List<RailtItemSequence> railItemSequences = new ArrayList<>();
	
	public Track(String name) {
		super();
		this.name = name;
	}
}