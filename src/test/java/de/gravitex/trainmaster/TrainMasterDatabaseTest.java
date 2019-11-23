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
    	
    	Station station = new Station("S1");
		stationRepository.save(station);

		Waggon waggon123 = new Waggon("123");
		railItemRepository.save(waggon123);
		
		Waggon waggon234 = new Waggon("234");
		railItemRepository.save(waggon234);
		
		Waggon waggon345 = new Waggon("345");
		railItemRepository.save(waggon345);
		
    	Track trackA = new Track("A");
    	trackA.setStation(station);
		trackRepository.save(trackA);
		
    	Track trackB = new Track("B");
    	trackB.setStation(station);
		trackRepository.save(trackB);

		putWaggonToTrack(waggon123, trackA);
		putWaggonToTrack(waggon234, trackA);
		putWaggonToTrack(waggon345, trackB);
    	
    	List<Track> allTracks = trackRepository.findAll();
		
		List<Track> findAll = allTracks;
		assertEquals(2, findAll.size());
		
		SimpleTrackRenderer simpleTrackRenderer = new SimpleTrackRenderer(allTracks);
		List<RailItem> railItems = null;
		for (Track t : allTracks) {
			railItems = railItemRepository.findByTrack(t);
			simpleTrackRenderer.putTrackWaggons(t, railItems);
		}
		
		simpleTrackRenderer.render();
		
		/*
		RailtItemSequence waggonSequenceAForExit = new RailItemSequenceBuilder()
				.withRailItems(new Waggon("WAG1"), new Waggon("WAG2"), new Waggon("WAG3")).build();
		assertEquals("WAG1@0#WAG2@1#WAG3@2", WaggonManager.getWaggonNumbersAsString(waggonSequenceAForExit));

		RailtItemSequence waggonSequenceBForExit = new RailItemSequenceBuilder().withRailItems(new Waggon("WAG4"), new Waggon("WAG5")).build();
		assertEquals("WAG4@0#WAG5@1", WaggonManager.getWaggonNumbersAsString(waggonSequenceBForExit));

		RailtItemSequence locomotiveSequence = new RailItemSequenceBuilder().withRailItems(new Locomotive("LOCO1"), new Locomotive("LOCO2")).build();

		// set up station A with waggons
		Track trackExitS1 = new Track("TExitS1");
		TrackPopulation trackPopulation = TrackManager.populateTrack(trackExitS1, locomotiveSequence, waggonSequenceAForExit, waggonSequenceBForExit);
		
		// save memberships
		for (RailtItemSequence waggonSequence : trackPopulation.getWaggonSequences()) {
			for (RailItemSequenceMembership membership : waggonSequence.getRailItemSequenceMemberships()) {
				railItemRepository.save(membership.getRailItem());
				waggonSequence.setRailtItemSequenceHolder(trackExitS1);
				railtItemSequenceRepository.save(waggonSequence);
				membership.setRailtItemSequence(waggonSequence);
				railtItemSequenceMembershipRepository.save(membership);				
			}
		}
		*/
    }

	private void putWaggonToTrack(Waggon waggon, Track track) {
		
		RailtItemSequence railtItemSequence = new RailtItemSequence();
		railtItemSequence.setRailtItemSequenceHolder(track);
		railItemSequenceRepository.save(railtItemSequence);

		RailItemSequenceMembership sequenceMembership = new RailItemSequenceMembership();
		sequenceMembership.setRailItem(waggon);
		sequenceMembership.setRailtItemSequence(railtItemSequence);
		railtItemSequenceMembershipRepository.save(sequenceMembership);
	}
}