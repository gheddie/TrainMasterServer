package de.gravitex.trainmaster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;

@DataJpaTest
public class TrainMasterDatabaseTest {

    @Autowired
    private StationRepository stationRepository;
    
    @Autowired
    private TrackRepository trackRepository;

    @Test
    public void testTrainRunner() throws Exception {
    	
    	Station station = new Station("S1");
		stationRepository.save(station);

    	Track track = new Track("123");
    	track.setStation(station);
		trackRepository.save(track);
    	
    	List<Track> findAll = trackRepository.findAll();
		assertEquals(1, findAll.size());
    }
}