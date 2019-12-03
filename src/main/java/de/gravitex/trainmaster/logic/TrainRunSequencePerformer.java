package de.gravitex.trainmaster.logic;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.manager.TrainRunManager;
import lombok.Data;

@Data
public class TrainRunSequencePerformer {

	private Track entryTrack;

	private Train train;

	public TrainRunSequencePerformer withArguments(Track exitTrack, RailItemSequence locomotiveSequence,
			RailItemSequence waggonSequenceForExit, Track aEntryTrack, Train aTrain) {

		train = aTrain;
		train.setWaggonSequence(waggonSequenceForExit);
		entryTrack = aEntryTrack;
		// train = TrainRunManager.prepareTrain(train, locomotiveSequence, waggonSequenceForExit);
		
		return this;
	}
	
	public Train depart() {
		train = TrainRunManager.departTrain(train);
		return train;
	}

	public Train arrive() {
		train = TrainRunManager.arriveTrain(train);
		return train;
	}
}