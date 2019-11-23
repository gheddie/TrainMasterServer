package de.gravitex.trainmaster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.gravitex.trainmaster.entity.Locomotive;
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.StationInfo;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.entity.Waggon;
import de.gravitex.trainmaster.entity.enumeration.TrainRunState;
import de.gravitex.trainmaster.helper.RailItemSequenceBuilder;
import de.gravitex.trainmaster.logic.TrainRunner;
import de.gravitex.trainmaster.manager.TrackManager;
import de.gravitex.trainmaster.manager.TrainRunManager;
import de.gravitex.trainmaster.manager.WaggonManager;

public class TrainRunTest {

	@Test
	public void testTrainRunWithWaggonsAndLocomotivesWithTrainRunner() {
		
		RailItemSequence waggonSequenceAForExit = new RailItemSequenceBuilder()
				.withRailItems(new Waggon("WAG1"), new Waggon("WAG2"), new Waggon("WAG3")).build();
		assertEquals("WAG1@0#WAG2@1#WAG3@2", WaggonManager.getWaggonNumbersAsString(waggonSequenceAForExit));

		RailItemSequence waggonSequenceBForExit = new RailItemSequenceBuilder().withRailItems(new Waggon("WAG4"), new Waggon("WAG5")).build();
		assertEquals("WAG4@0#WAG5@1", WaggonManager.getWaggonNumbersAsString(waggonSequenceBForExit));

		RailItemSequence locomotiveSequence = new RailItemSequenceBuilder().withRailItems(new Locomotive("LOCO1"), new Locomotive("LOCO2")).build();

		// set up station A with waggons
		Track trackExitS1 = new Track("TExitS1");
		TrackManager.populateTrack(trackExitS1, locomotiveSequence, waggonSequenceAForExit, waggonSequenceBForExit);

		assertEquals(TrackManager.getRailItemIdetifiersAsString(trackExitS1), "LOCO1@0#LOCO2@1#WAG1@2#WAG2@3#WAG3@4#WAG4@5#WAG5@6");
		
		TrainRunner trainRunner = new TrainRunner();
		trainRunner.withArguments(trackExitS1, locomotiveSequence, waggonSequenceAForExit, new Track("TEntryS2"));
		
		trainRunner.depart();
		trainRunner.arrive();
		
		// waggons must be on the entry track in station S2!!
		assertEquals("LOCO1@0#LOCO2@1#WAG1@2#WAG2@3#WAG3@4", TrackManager.getRailItemIdetifiersAsString(trainRunner.getEntryTrack()));
	}

	@Test
	public void testTrainRunWithWaggonsAndLocomotives() {

		RailItemSequence waggonSequenceAForExit = new RailItemSequenceBuilder()
				.withRailItems(new Waggon("WAG1"), new Waggon("WAG2"), new Waggon("WAG3")).build();
		assertEquals("WAG1@0#WAG2@1#WAG3@2", WaggonManager.getWaggonNumbersAsString(waggonSequenceAForExit));

		RailItemSequence waggonSequenceBForExit = new RailItemSequenceBuilder().withRailItems(new Waggon("WAG4"), new Waggon("WAG5")).build();
		assertEquals("WAG4@0#WAG5@1", WaggonManager.getWaggonNumbersAsString(waggonSequenceBForExit));

		RailItemSequence locomotiveSequence = new RailItemSequenceBuilder().withRailItems(new Locomotive("LOCO1"), new Locomotive("LOCO2")).build();

		// set up station A with waggons
		Track trackExitS1 = new Track("TExitS1");
		TrackManager.populateTrack(trackExitS1, locomotiveSequence, waggonSequenceAForExit, waggonSequenceBForExit);

		assertEquals(TrackManager.getRailItemIdetifiersAsString(trackExitS1), "LOCO1@0#LOCO2@1#WAG1@2#WAG2@3#WAG3@4#WAG4@5#WAG5@6");

		Train train = new Train();
		train.setWaggonSequence(waggonSequenceAForExit);

		Track trackEntryS2 = new Track("TEntryS2");

		TrainRun trainRun = TrainRun.fromStationNames(new StationInfo(new Station("S1"), null, trackExitS1),
				new StationInfo(new Station("S2"), trackEntryS2, null));
		train.setTrainRun(trainRun);

		train = TrainRunManager.prepareTrain(train, locomotiveSequence, waggonSequenceAForExit);
		assertEquals(0, train.getTrainRun().getTrainRunSectionIndex());

		train = TrainRunManager.departTrain(train);
		train = TrainRunManager.arriveTrain(train);

		// waggons must be on the entry track in station S2!!
		assertEquals("LOCO1@0#LOCO2@1#WAG1@2#WAG2@3#WAG3@4", TrackManager.getRailItemIdetifiersAsString(trackEntryS2));
	}

	@Test
	public void testSimpleTrainRun() {

		Train train = new Train();

		// exit tracks
		Track exitTrackS1 = new Track("TExit1");
		Track exitTrackS2 = new Track("TExit2");
		Track exitTrackS3 = new Track("TExit3");

		TrainRun trainRun = TrainRun.fromStationNames(new StationInfo(new Station("S1"), new Track("TEntryS1"), exitTrackS1),
				new StationInfo(new Station("S2"), new Track("TEntryS2"), exitTrackS2),
				new StationInfo(new Station("S3"), new Track("TEntryS3"), exitTrackS3));
		train.setTrainRun(trainRun);

		RailItemSequence locomotiveSequence = new RailItemSequenceBuilder().withRailItems(new Locomotive("LOCO1"), new Locomotive("LOCO2")).build();

		TrackManager.populateTrack(exitTrackS1, locomotiveSequence);

		train = TrainRunManager.prepareTrain(train, locomotiveSequence);
		assertEquals(TrainRunState.PREPARED, train.getTrainRun().getTrainRunState());
		assertTrue(train.getActualStationName().equals("S1"));

		// leave 'S1'...
		train = TrainRunManager.departTrain(train);
		assertEquals(TrainRunState.DEPARTED, train.getTrainRun().getTrainRunState());
		assertTrue(train.getActualStationName() == null);
		// arrive at 'S2'...
		train = TrainRunManager.arriveTrain(train);
		assertEquals(TrainRunState.ARRIVED, train.getTrainRun().getTrainRunState());
		assertEquals("S2", train.getActualStationName());

		// leave 'S2'...
		train = TrainRunManager.departTrain(train);
		assertEquals(TrainRunState.DEPARTED, train.getTrainRun().getTrainRunState());
		assertTrue(train.getActualStationName() == null);
		// arrive at 'S3'...
		train = TrainRunManager.arriveTrain(train);
		assertEquals("S3", train.getActualStationName());

		assertEquals(TrainRunState.FINSHED, train.getTrainRun().getTrainRunState());
	}

	/*
	// @Test(expected = TrainRunException.class)
	// TODO
	@Test
	public void testTrainRunExitingWithoutLocomotive() {

		RailtItemSequence sequenceTrack1 = new RailItemSequenceBuilder().withRailItems(new Waggon("WAG1"), new Waggon("WAG2"), new Waggon("WAG3"))
				.build();
		assertEquals("WAG1@0#WAG2@1#WAG3@2", WaggonManager.getWaggonNumbersAsString(sequenceTrack1));

		RailtItemSequence sequenceTrack2 = new RailItemSequenceBuilder().withRailItems(new Waggon("WAG4"), new Waggon("WAG5")).build();
		assertEquals("WAG4@0#WAG5@1", WaggonManager.getWaggonNumbersAsString(sequenceTrack2));

		// set up station A with waggons
		Track trackExitS1 = new Track("TExitS1");
		TrackManager.populateTrack(trackExitS1, null, sequenceTrack1, sequenceTrack2);

		Train train = new Train();
		train.setWaggonSequence(sequenceTrack1);

		Track trackEntryS2 = new Track("TEntryS2");

		TrainRun trainRun = TrainRun.fromStationNames(new StationInfo(new Station("S1"), null, trackExitS1),
				new StationInfo(new Station("S2"), trackEntryS2, null));
		train.setTrainRun(trainRun);

		train = TrainRunManager.prepareTrain(train, null, sequenceTrack1);
		assertEquals(0, train.getTrainRun().getTrainRunSectionIndex());

		train = TrainRunManager.departTrain(train);
	}
	*/
}