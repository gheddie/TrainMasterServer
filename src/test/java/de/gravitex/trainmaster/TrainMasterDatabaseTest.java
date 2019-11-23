package de.gravitex.trainmaster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.RailtItemSequence;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Waggon;
import de.gravitex.trainmaster.repo.RailItemRepository;
import de.gravitex.trainmaster.repo.RailtItemSequenceMembershipRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.util.SimpleTrackRenderer;

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

	@Test
	public void testTrainRunner() throws Exception {

		Station station1 = new Station("S1");
		stationRepository.save(station1);

		Station station2 = new Station("S2");
		stationRepository.save(station2);

		Waggon waggon123 = new Waggon("123");
		railItemRepository.save(waggon123);

		Waggon waggon234 = new Waggon("234");
		railItemRepository.save(waggon234);

		Waggon waggon345 = new Waggon("345");
		railItemRepository.save(waggon345);

		Waggon waggon456 = new Waggon("456");
		railItemRepository.save(waggon456);

		Waggon waggon567 = new Waggon("567");
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

		putWaggonToTrack(waggon123, track1Station1);
		putWaggonToTrack(waggon234, track1Station1);
		putWaggonToTrack(waggon345, track2Station1);
		putWaggonToTrack(waggon456, track1Station2);
		putWaggonToTrack(waggon567, track1Station2);

		List<Station> allStations = stationRepository.findAll();
		assertEquals(2, allStations.size());

		assertEquals(3, trackRepository.findAll().size());

		SimpleTrackRenderer simpleTrackRenderer = new SimpleTrackRenderer();

		List<RailItem> railItems = null;
		for (Track t : trackRepository.findAll()) {
			railItems = railItemRepository.findByTrack(t);
			simpleTrackRenderer.putTrackWaggons(t, railItems);
		}

		simpleTrackRenderer.render();
	}

	private void putWaggonToTrack(Waggon waggon, Track track) {

		System.out.println("putWaggonToTrack :: waggon = " + waggon.getWaggonNumber() + ", track = " + track.getName()
				+ ", station = " + track.getStation().getStationName());

		RailtItemSequence railtItemSequence = new RailtItemSequence();
		railtItemSequence.setRailtItemSequenceHolder(track);
		railItemSequenceRepository.save(railtItemSequence);

		RailItemSequenceMembership sequenceMembership = new RailItemSequenceMembership();
		sequenceMembership.setRailItem(waggon);
		sequenceMembership.setRailtItemSequence(railtItemSequence);
		railtItemSequenceMembershipRepository.save(sequenceMembership);
	}
}