package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Train extends RailItemSequenceHolder {
	
	@OneToOne
	public RailItemSequence locomotives;

	@OneToOne
	public RailItemSequence waggonSequence;
	
	@OneToOne
	public TrainRun trainRun;
	
	@OneToOne
	public Station actualStation;
	
	public String getActualStationName() {
		if (actualStation == null) {
			return null;
		}
		return actualStation.getStationName();
	}
}