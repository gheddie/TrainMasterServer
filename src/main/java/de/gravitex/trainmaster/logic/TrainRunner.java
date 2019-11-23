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

	public void runTrain(Track exitTrack, RailItemSequence locomotiveSequence, RailItemSequence waggonSequenceForExit, Track aEntryTrack) {
		
		Train train = new Train();
		train.setWaggonSequence(waggonSequenceForExit);

		entryTrack = aEntryTrack;

		TrainRun trainRun = TrainRun.fromStationNames(new StationInfo(new Station("S1"), null, exitTrack),
				new StationInfo(new Station("S2"), entryTrack, null));
		train.setTrainRun(trainRun);

		train = TrainRunManager.prepareTrain(train, locomotiveSequence, waggonSequenceForExit);
		
		train = TrainRunManager.departTrain(train);
		train = TrainRunManager.arriveTrain(train);
	}
}