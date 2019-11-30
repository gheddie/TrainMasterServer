package de.gravitex.trainmaster.logic;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.manager.TrainRunManager;
import lombok.Data;

@Data
public class TrainRunSsequencePerformer {

	private Track entryTrack;

	private Train train;

	public void withArguments(Track exitTrack, RailItemSequence locomotiveSequence,
			RailItemSequence waggonSequenceForExit, Track aEntryTrack, Train aTrain) {

		train = aTrain;
		train.setWaggonSequence(waggonSequenceForExit);
		entryTrack = aEntryTrack;
		train = TrainRunManager.prepareTrain(train, locomotiveSequence, waggonSequenceForExit);
	}

	public Train arrive() {
		train = TrainRunManager.arriveTrain(train);
		return train;
	}

	public Train depart() {
		train = TrainRunManager.departTrain(train);
		return train;
	}
}