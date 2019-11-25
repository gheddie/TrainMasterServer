package de.gravitex.trainmaster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.gravitex.trainmaster.dlh.EntityHelper;
import de.gravitex.trainmaster.dlh.SimpleTrackRenderer;
import de.gravitex.trainmaster.entity.Locomotive;
import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.StationInfo;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.entity.TrainRunSection;
import de.gravitex.trainmaster.entity.Waggon;
import de.gravitex.trainmaster.logic.TrainRunner;
import de.gravitex.trainmaster.repo.RailItemRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.RailtItemSequenceMembershipRepository;
import de.gravitex.trainmaster.repo.StationInfoRepository;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.repo.TrainRepository;
import de.gravitex.trainmaster.repo.TrainRunRepository;
import de.gravitex.trainmaster.repo.TrainRunSectionRepository;

@DataJpaTest
public class TrainMasterDatabaseTest {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private RailItemSequenceRepository railItemSequenceRepository;

	@Autowired
	private RailtItemSequenceMembershipRepository railtItemSequenceMembershipRepository;

	@Autowired
	private RailItemRepository railItemRepository;

	@Autowired
	private TrainRepository trainRepository;

	@Autowired
	private TrainRunRepository trainRunRepository;

	@Autowired
	private TrainRunSectionRepository trainRunSectionRepository;

	@Autowired
	private StationInfoRepository stationInfoRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	public void testTrainRunner() throws Exception {

		Station station1 = new Station("S1");
		stationRepository.save(station1);

		Station station2 = new Station("S2");
		stationRepository.save(station2);

		Locomotive locomotive1 = EntityHelper.makeLocomotive("L1");
		railItemRepository.save(locomotive1);

		Waggon waggon123 = EntityHelper.makeWaggon("123");
		railItemRepository.save(waggon123);

		Waggon waggon234 = EntityHelper.makeWaggon("234");
		railItemRepository.save(waggon234);

		Waggon waggon345 = EntityHelper.makeWaggon("345");
		railItemRepository.save(waggon345);

		Waggon waggon456 = EntityHelper.makeWaggon("456");
		railItemRepository.save(waggon456);

		Waggon waggon567 = EntityHelper.makeWaggon("567");
		railItemRepository.save(waggon567);

		Track track1Station1 = new Track("track1Station1");
		track1Station1.setStation(station1);
		trackRepository.save(track1Station1);

		Track track2Station1 = new Track("track2Station1");
		track2Station1.setStation(station1);
		trackRepository.save(track2Station1);

		Track track1Station2 = new Track("track1Station2");
		track1Station2.setStation(station2);
		trackRepository.save(track1Station2);

		RailItemSequence seqLocos = new RailItemSequence(0);
		railItemSequenceRepository.save(seqLocos);
		RailItemSequence seqTrack1Station1 = new RailItemSequence(1);
		railItemSequenceRepository.save(seqTrack1Station1);
		RailItemSequence seqTrack2Station1 = new RailItemSequence(0);
		railItemSequenceRepository.save(seqTrack2Station1);
		RailItemSequence seqTrack1Station2 = new RailItemSequence(0);
		railItemSequenceRepository.save(seqTrack1Station2);

		putRailItemToTrack(locomotive1, track1Station1, seqLocos, 0);
		putRailItemToTrack(waggon123, track1Station1, seqTrack1Station1, 0);
		putRailItemToTrack(waggon234, track1Station1, seqTrack1Station1, 1);
		putRailItemToTrack(waggon345, track2Station1, seqTrack2Station1, 0);
		putRailItemToTrack(waggon456, track1Station2, seqTrack1Station2, 0);
		putRailItemToTrack(waggon567, track1Station2, seqTrack1Station2, 1);

		List<Station> allStations = stationRepository.findAll();
		assertEquals(2, allStations.size());

		assertEquals(3, trackRepository.findAll().size());

		renderTracksAndWaggons("BEFORE");

		// run train
		TrainRunner trainRunner = new TrainRunner();

		// create train with a train run Station 1 -> Station 2
		StationInfo stationInfo1 = new StationInfo(station1, null, track1Station1);
		stationInfoRepository.save(stationInfo1);
		StationInfo stationInfo2 = new StationInfo(station2, track1Station2, null);
		stationInfoRepository.save(stationInfo2);
		TrainRunSection sec1 = new TrainRunSection(stationInfo1, stationInfo2);
		trainRunSectionRepository.save(sec1);
		Train train = new Train();
		TrainRun trainRun = new TrainRun();
		trainRunRepository.save(trainRun);
		train.setTrainRun(trainRun);
		trainRun.getTrainRunSections().add(sec1);
		trainRepository.save(train);

		trainRunner.withArguments(track1Station1, seqLocos, seqTrack1Station1, track1Station2, train);

		trainRunner.depart();
		entityManager.flush();
		// persistTrainRunner(trainRunner);
		renderTracksAndWaggons("AFTER DEPART");

		trainRunner.arrive();
		entityManager.flush();
		// persistTrainRunner(trainRunner);
		renderTracksAndWaggons("AFTER ARRIVAL");
	}

	private void persistTrainRunner(TrainRunner trainRunner) {
		trainRepository.save(trainRunner.getTrain());
		if (trainRunner.getTrain().getActualStation() != null) {
			stationRepository.save(trainRunner.getTrain().getActualStation());
		}
		trackRepository.save(trainRunner.getEntryTrack());
		stationRepository.save(trainRunner.getEntryTrack().getStation());
		for (TrainRunSection trainRunSection : trainRunner.getTrain().getTrainRun().getTrainRunSections()) {
			trainRunSectionRepository.save(trainRunSection);
			stationRepository.save(trainRunSection.getStationFrom().getStation());
			stationRepository.save(trainRunSection.getStationTo().getStation());
			stationInfoRepository.save(trainRunSection.getStationFrom());
			stationInfoRepository.save(trainRunSection.getStationTo());
		}
		trainRunRepository.save(trainRunner.getTrain().getTrainRun());
		entityManager.flush();
	}

	private String renderTracksAndWaggons(String description) {
		List<RailItemSequenceMembership> railItems = null;
		SimpleTrackRenderer simpleTrackRenderer = new SimpleTrackRenderer();
		simpleTrackRenderer.setDescription(description);
		for (Track t : trackRepository.findAll()) {
			railItems = railItemRepository.findByTrack(t);
			simpleTrackRenderer.putTrackWaggons(t, railItems);
		}
		return simpleTrackRenderer.render();
	}

	private void putRailItemToTrack(RailItem railItem, Track track, RailItemSequence railtItemSequence,
			int ordinalPosition) {

		System.out.println("putWaggonToTrack :: waggon = " + railItem.getIdentifier() + ", track = " + track.getName()
				+ ", station = " + track.getStation().getStationName());

		railtItemSequence.setRailItemSequenceHolder(track);
		railItemSequenceRepository.save(railtItemSequence);

		RailItemSequenceMembership sequenceMembership = new RailItemSequenceMembership();
		sequenceMembership.setRailItem(railItem);
		sequenceMembership.setOrdinalPosition(ordinalPosition);
		sequenceMembership.setRailItemSequence(railtItemSequence);
		railtItemSequenceMembershipRepository.save(sequenceMembership);
	}
}