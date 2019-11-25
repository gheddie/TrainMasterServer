package de.gravitex.trainmaster.logic;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.StationInfo;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.manager.TrainRunManager;
import lombok.Data;

@Data
public class TrainRunner {

	private Track entryTrack;
	
	private Train train;

	public void withArguments(Track exitTrack, RailItemSequence locomotiveSequence, RailItemSequence waggonSequenceForExit, Track aEntryTrack, Train aTrain) {
		
		// train = new Train();
		
		train = aTrain;
		train.setWaggonSequence(waggonSequenceForExit);

		entryTrack = aEntryTrack;

		/*
		TrainRun trainRun = TrainRun.fromStationNames(new StationInfo(new Station("S1"), null, exitTrack),
				new StationInfo(new Station("S2"), entryTrack, null));
		train.setTrainRun(trainRun);
		*/

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