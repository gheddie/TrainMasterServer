package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class TrainRunSection extends BaseEntity {

	@OneToOne
	private StationInfo stationFrom;
	
	@OneToOne
	private StationInfo stationTo;
	
	public TrainRunSection(StationInfo stationFrom, StationInfo stationTo) {
		super();
		this.stationFrom = stationFrom;
		this.stationTo = stationTo;
	}
}