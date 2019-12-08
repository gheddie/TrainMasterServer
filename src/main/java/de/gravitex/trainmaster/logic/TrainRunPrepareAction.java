package de.gravitex.trainmaster.logic;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.enumeration.TrainState;
import de.gravitex.trainmaster.entity.trainrun.TrainRun;
import de.gravitex.trainmaster.request.TrainRunDescriptor;
import lombok.Data;

@Data
public class TrainRunPrepareAction extends TrainRunAction {

	private TrainRunDescriptor trainRunDescriptor;
	
	private RailItemSequence sequence;

	private TrainRun trainRun;

	private Train train;

	public void execute() {
		
		trainRun = new TrainRun();

		train = new Train();
		train.setTrainState(TrainState.PREPARED);
		train.setTrainNumber(trainRunDescriptor.getTrainNumber());
		
		train.setWaggonSequence(sequence);
		train.setTrainRun(trainRun);
	}
}