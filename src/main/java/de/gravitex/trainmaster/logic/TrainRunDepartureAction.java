package de.gravitex.trainmaster.logic;

import de.gravitex.trainmaster.entity.Train;
import lombok.Data;

@Data
public class TrainRunDepartureAction extends TrainRunAction {

	private Train train;

	@Override
	public void execute() {
		
		// remove train waggon sequnce from track
		train.getWaggonSequence().setTrack(null);
	}
}