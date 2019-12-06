package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.gravitex.trainmaster.entity.enumeration.TrainState;
import lombok.Data;

@Entity
@Data
public class Train extends RailItemSequenceHolder {
	
	@OneToOne
	public RailItemSequence locomotives;
	
	@NotBlank
	public String trainNumber;

	@NotNull
	@OneToOne
	public RailItemSequence waggonSequence;
	
	@OneToOne
	public TrainRun trainRun;
	
	@NotNull
	public TrainState trainState;
	
	@OneToOne
	public Station actualStation;
	
	public String getActualStationName() {
		if (actualStation == null) {
			return null;
		}
		return actualStation.getStationName();
	}
}