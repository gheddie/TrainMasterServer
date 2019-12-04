package de.gravitex.trainmaster.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.gravitex.trainmaster.checker.OverweightTrainChecker;
import de.gravitex.trainmaster.checker.TrainChecker;
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.entity.TrainRunSection;
import de.gravitex.trainmaster.entity.enumeration.TrainRunState;
import de.gravitex.trainmaster.exception.TrainRunException;

public class TrainRunManager {

	public static Train departTrain(Train train) {
		if (train.getLocomotives() == null) {
			throw new TrainRunException("train must have locos!!");
		}
		performChecks(train);
		System.out.println("train is leaving station: " + train.getActualStationName());
		train.setActualStation(null);
		train.getTrainRun().setTrainRunState(TrainRunState.DEPARTED);
		return train;
	}

	public static Train arriveTrain(Train train) {
		TrainRun trainRun = train.getTrainRun();
		TrainRunSection trainRunSectionByIndex = trainRun.getTrainRunSectionByIndex();
		Track entryTrack = trainRunSectionByIndex.getStationTo().getEntryTrack();
		if (entryTrack == null) {
			throw new TrainRunException("There must an entry track!!");
		}
		trainRun.setTrainRunState(TrainRunState.ARRIVED);
		// switch locos to entry track (if any)
		if (train.getLocomotives() != null) {
			train.getLocomotives().setTrack(entryTrack);
			entryTrack.getRailItemSequences().add(train.getLocomotives());
		}
		// switch waggons to entry track (if any)
		if (train.getWaggonSequence() != null) {
			train.getWaggonSequence().setTrack(entryTrack);
			entryTrack.getRailItemSequences().add(train.getWaggonSequence());
		}
		train.setActualStation(trainRunSectionByIndex.getStationTo().getStation());
		if (trainRun.getFinalIndex()) {
			trainRun.setTrainRunState(TrainRunState.FINSHED);
		} else {
			trainRun.increaseTrainRunSectionIndex();
		}
		System.out.println("train is arriving at station: " + train.getActualStationName());
		return train;
	}

	public static Train prepareTrain(Train train, RailItemSequence locomotiveSequence, RailItemSequence waggonSequenceForExit) throws TrainRunException {

		if (train.getTrainRun() == null) {
			throw new TrainRunException("train must have a train run set!!");
		}
		// locos must be on the same track as the waggons
		Track railItemTrack = null;
		HashSet<Track> trackSet = new HashSet<>();
		if (locomotiveSequence != null) {
			railItemTrack = (Track) locomotiveSequence.getTrack();
			if (railItemTrack == null) {
				throw new TrainRunException("loco sequence without a track detected!!");
			}
			trackSet.add(railItemTrack);
		}
		railItemTrack = (Track) waggonSequenceForExit.getTrack();
		if (railItemTrack == null) {
			throw new TrainRunException("waggon sequence without a track detected!!");
		}
		trackSet.add(railItemTrack);
		// there must be exactly one track (for locos and waggons)!!
		if (!(trackSet.size() == 1)) {
			throw new TrainRunException("more than one track detected on preparing a train!!");
		}
		
		train.setLocomotives(locomotiveSequence);
		// switch loco(s) to train!!
		if (locomotiveSequence != null) {
			locomotiveSequence.setTrain(train);
		}
		// switch waggon sequences to the train!!
		waggonSequenceForExit.setTrain(train);
		train.getTrainRun().setTrainRunState(TrainRunState.PREPARED);
		train.setActualStation(train.getTrainRun().getTrainRunSectionByIndex().getStationFrom().getStation());
		return train;
	}

	private static void performChecks(Train train) {
		for (TrainChecker checker : TrainRunManager.getCheckers()) {
			checker.check();
		}
	}

	private static List<TrainChecker> getCheckers() {
		ArrayList<TrainChecker> result = new ArrayList<>();
		result.add(new OverweightTrainChecker());
		return result;
	}
}