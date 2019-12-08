package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class RailItemSequence extends BaseEntity {
	
	@NotNull
	@Column(name = "ordinal_position")
	private Integer ordinalPosition;
	
	@NotBlank
	private String sequenceIdentifier;
	
	@OneToOne
	private Track track;
	
	@OneToOne
	private Train train;

	@OneToMany
	private List<RailItemSequenceMembership> railItemSequenceMemberships = new ArrayList<>();
}