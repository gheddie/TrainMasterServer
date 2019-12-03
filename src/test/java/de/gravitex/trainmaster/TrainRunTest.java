package de.gravitex.trainmaster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.gravitex.trainmaster.dlh.EntityHelper;
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.StationInfo;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.entity.TrainRunSection;
import de.gravitex.trainmaster.helper.RailItemSequenceBuilder;
import de.gravitex.trainmaster.logic.TrainRunSequencePerformer;
import de.gravitex.trainmaster.manager.TrackManager;
import de.gravitex.trainmaster.manager.TrainRunManager;
import de.gravitex.trainmaster.manager.WaggonManager;

public class TrainRunTest {

	@Test
	public void testTrainRunWithWaggonsAndLocomotivesWithTrainRunner() {

		RailItemSequence waggonSequenceAForExit = new RailItemSequenceBuilder()
				.withRailItems(EntityHelper.makeWaggon("WAG1"), EntityHelper.makeWaggon("WAG2"),
						EntityHelper.makeWaggon("WAG3"))
				.build();
		assertEquals("WAG1@0#WAG2@1#WAG3@2", WaggonManager.getWaggonNumbersAsString(waggonSequenceAForExit));

		RailItemSequence waggonSequenceBForExit = new RailItemSequenceBuilder()
				.withRailItems(EntityHelper.makeWaggon("WAG4"), EntityHelper.makeWaggon("WAG5")).build();
		assertEquals("WAG4@0#WAG5@1", WaggonManager.getWaggonNumbersAsString(waggonSequenceBForExit));

		RailItemSequence locomotiveSequence = new RailItemSequenceBuilder()
				.withRailItems(EntityHelper.makeLocomotive("LOCO1"), EntityHelper.makeLocomotive("LOCO2")).build();

		// set up station A with waggons
		Track trackExitS1 = new Track();
		trackExitS1.setTrackNumber("TExitS1");
		TrackManager.populateTrack(trackExitS1, locomotiveSequence, waggonSequenceAForExit, waggonSequenceBForExit);

		assertEquals(TrackManager.getRailItemIdetifiersAsString(trackExitS1),
				"LOCO1@0#LOCO2@1#WAG1@2#WAG2@3#WAG3@4#WAG4@5#WAG5@6");

		TrainRunSequencePerformer trainRunner = new TrainRunSequencePerformer();
		Train aTrain = new Train();
		TrainRun trainRun = new TrainRun();
		Station stationS1 = new Station();
		stationS1.setStationName("S1");
		
		StationInfo stationFrom = new StationInfo();
		stationFrom.setStation(stationS1);
		stationFrom.setExitTrack(trackExitS1);
		
		Track trackEntryS2 = new Track();
		trackEntryS2.setTrackNumber("TEntryS2");
		Station stationS2 = new Station();
		stationS2.setStationName("S2");
		
		StationInfo stationTo = new StationInfo();
		stationTo.setStation(stationS2);
		stationTo.setEntryTrack(trackEntryS2);
		
		TrainRunSection trainRunSection = new TrainRunSection();
		trainRunSection.setStationFrom(stationFrom);
		trainRunSection.setStationTo(stationTo);
		trainRun.getTrainRunSections().add(trainRunSection);
		aTrain.setTrainRun(trainRun);
		trainRunner.withArguments(trackExitS1, locomotiveSequence, waggonSequenceAForExit, trackEntryS2, aTrain);

		trainRunner.depart();
		trainRunner.arrive();

		// waggons must be on the entry track in station S2!!
		assertEquals("LOCO1@0#LOCO2@1#WAG1@2#WAG2@3#WAG3@4",
				TrackManager.getRailItemIdetifiersAsString(trainRunner.getEntryTrack()));
	}

	@Test
	public void testTrainRunWithWaggonsAndLocomotives() {

		RailItemSequence waggonSequenceAForExit = new RailItemSequenceBuilder()
				.withRailItems(EntityHelper.makeWaggon("WAG1"), EntityHelper.makeWaggon("WAG2"),
						EntityHelper.makeWaggon("WAG3"))
				.build();
		
		assertEquals("WAG1@0#WAG2@1#WAG3@2", WaggonManager.getWaggonNumbersAsString(waggonSequenceAForExit));

		RailItemSequence waggonSequenceBForExit = new RailItemSequenceBuilder()
				.withRailItems(EntityHelper.makeWaggon("WAG4"), EntityHelper.makeWaggon("WAG5")).build();
		assertEquals("WAG4@0#WAG5@1", WaggonManager.getWaggonNumbersAsString(waggonSequenceBForExit));

		RailItemSequence locomotiveSequence = new RailItemSequenceBuilder()
				.withRailItems(EntityHelper.makeLocomotive("LOCO1"), EntityHelper.makeLocomotive("LOCO2")).build();

		// set up station A with waggons
		Track trackExitS1 = new Track();
		trackExitS1.setTrackNumber("TExitS1");
		TrackManager.populateTrack(trackExitS1, locomotiveSequence, waggonSequenceAForExit, waggonSequenceBForExit);

		assertEquals(TrackManager.getRailItemIdetifiersAsString(trackExitS1),
				"LOCO1@0#LOCO2@1#WAG1@2#WAG2@3#WAG3@4#WAG4@5#WAG5@6");

		Train train = new Train();
		train.setWaggonSequence(waggonSequenceAForExit);

		Track trackEntryS2 = new Track();
		trackEntryS2.setTrackNumber("TEntryS2");

		Station stationS1 = new Station();
		stationS1.setStationName("S1");
		Station stationS2 = new Station();
		stationS2.setStationName("S2");
		
		StationInfo stationInfo1 = new StationInfo();
		stationInfo1.setStation(stationS1);
		stationInfo1.setExitTrack(trackExitS1);
		
		StationInfo stationInfo2 = new StationInfo();
		stationInfo2.setStation(stationS2);
		stationInfo2.setEntryTrack(trackEntryS2);
		
		TrainRun trainRun = TrainRun.fromStationNames(stationInfo1,
				stationInfo2);
		train.setTrainRun(trainRun);

		train = TrainRunManager.prepareTrain(train, locomotiveSequence, waggonSequenceAForExit);
		assertEquals(0, train.getTrainRun().getActualTrainRunSectionIndex());

		train = TrainRunManager.departTrain(train);
		train = TrainRunManager.arriveTrain(train);

		// waggons must be on the entry track in station S2!!
		assertEquals("LOCO1@0#LOCO2@1#WAG1@2#WAG2@3#WAG3@4", TrackManager.getRailItemIdetifiersAsString(trackEntryS2));
	}

	/*
	 * @Test public void testSimpleTrainRun() {
	 * 
	 * Train train = new Train();
	 * 
	 * // exit tracks Track exitTrackS1 = new Track("TExit1"); Track exitTrackS2 =
	 * new Track("TExit2"); Track exitTrackS3 = new Track("TExit3");
	 * 
	 * TrainRun trainRun = TrainRun.fromStationNames(new StationInfo(new
	 * Station("S1"), new Track("TEntryS1"), exitTrackS1), new StationInfo(new
	 * Station("S2"), new Track("TEntryS2"), exitTrackS2), new StationInfo(new
	 * Station("S3"), new Track("TEntryS3"), exitTrackS3));
	 * train.setTrainRun(trainRun);
	 * 
	 * RailItemSequence locomotiveSequence = new
	 * RailItemSequenceBuilder().withRailItems(new Locomotive("LOCO1"), new
	 * Locomotive("LOCO2")).build();
	 * 
	 * TrackManager.populateTrack(exitTrackS1, locomotiveSequence);
	 * 
	 * train = TrainRunManager.prepareTrain(train, locomotiveSequence, null);
	 * assertEquals(TrainRunState.PREPARED, train.getTrainRun().getTrainRunState());
	 * assertTrue(train.getActualStationName().equals("S1"));
	 * 
	 * // leave 'S1'... train = TrainRunManager.departTrain(train);
	 * assertEquals(TrainRunState.DEPARTED, train.getTrainRun().getTrainRunState());
	 * assertTrue(train.getActualStationName() == null); // arrive at 'S2'... train
	 * = TrainRunManager.arriveTrain(train); assertEquals(TrainRunState.ARRIVED,
	 * train.getTrainRun().getTrainRunState()); assertEquals("S2",
	 * train.getActualStationName());
	 * 
	 * // leave 'S2'... train = TrainRunManager.departTrain(train);
	 * assertEquals(TrainRunState.DEPARTED, train.getTrainRun().getTrainRunState());
	 * assertTrue(train.getActualStationName() == null); // arrive at 'S3'... train
	 * = TrainRunManager.arriveTrain(train); assertEquals("S3",
	 * train.getActualStationName());
	 * 
	 * assertEquals(TrainRunState.FINSHED, train.getTrainRun().getTrainRunState());
	 * }
	 */
}