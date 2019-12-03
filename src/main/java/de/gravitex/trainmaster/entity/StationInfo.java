package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class StationInfo extends BaseEntity {

	@NotNull
	@OneToOne
	private Station station;
	
	@OneToOne
	private Track entryTrack;
	
	@OneToOne
	private Track exitTrack;
}