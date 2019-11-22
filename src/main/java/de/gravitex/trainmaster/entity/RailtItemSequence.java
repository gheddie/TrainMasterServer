package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class RailtItemSequence extends BaseEntity {
	
	@OneToOne
	private Track track;
	
	@OneToOne
	private RailtItemSequenceHolder railtItemSequenceHolder;

	@OneToMany
	private List<RailItemSequenceMembership> railItemSequenceMemberships = new ArrayList<>();
}