package de.gravitex.trainmaster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.gravitex.trainmaster.entity.Locomotive;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.RailtItemSequence;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Waggon;
import de.gravitex.trainmaster.helper.RailItemSequenceBuilder;
import de.gravitex.trainmaster.logic.dlh.TrackPopulation;
import de.gravitex.trainmaster.logic.manager.TrackManager;
import de.gravitex.trainmaster.logic.manager.WaggonManager;
import de.gravitex.trainmaster.repo.RailItemRepository;
import de.gravitex.trainmaster.repo.RailtItemSequenceMembershipRepository;
import de.gravitex.trainmaster.repo.RailtItemSequenceRepository;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;

@DataJpaTest
public class TrainMasterDatabaseTest {

    @Autowired
    private StationRepository stationRepository;
    
    @Autowired
    private TrackRepository trackRepository;
    
    @Autowired
    private RailtItemSequenceRepository railtItemSequenceRepository;
    
    @Autowired
    private RailtItemSequenceMembershipRepository railtItemSequenceMembershipRepository;
    
    @Autowired
    private RailItemRepository railItemRepository;

    @Test
    public void testTrainRunner() throws Exception {
    	
    	Station station = new Station("S1");
		stationRepository.save(station);

    	Track track = new Track("123");
    	track.setStation(station);
		trackRepository.save(track);
    	
    	List<Track> findAll = trackRepository.findAll();
		assertEquals(1, findAll.size());
		
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
		
		List<Track> tracks = trackRepository.findAll();
		for (Track t : tracks) {
			System.out.println(" ------------------ track ------------------ ");
		}
    }
}