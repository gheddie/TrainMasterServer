package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class TrainRunSection extends BaseEntity {
	
	@NotNull
	@ManyToOne
	private TrainRun trainRun;

	@NotNull
	@OneToOne
	private StationInfo stationFrom;

	@NotNull
	@OneToOne
	private StationInfo stationTo;
}