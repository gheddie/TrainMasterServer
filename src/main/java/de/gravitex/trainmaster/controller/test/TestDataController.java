package de.gravitex.trainmaster.controller.test;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dlh.EntityHelper;
import de.gravitex.trainmaster.dlh.SimpleTrackRenderer;
import de.gravitex.trainmaster.entity.Locomotive;
import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Waggon;
import de.gravitex.trainmaster.repo.RailItemRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceMembershipRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.repo.WaggonRepository;

@RestController
public class TestDataController {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private RailItemSequenceRepository railItemSequenceRepository;

	@Autowired
	private RailItemSequenceMembershipRepository railItemSequenceMembershipRepository;

	@Autowired
	private RailItemRepository railItemRepository;

	@Autowired
	WaggonRepository waggonRepository;

	@Transactional
	@RequestMapping(ServerMappings.TestData.CREATION)
	public void creation() {

		Station station1 = new Station();
		station1.setStationName("S1");
		stationRepository.save(station1);

		Station station2 = new Station();
		station2.setStationName("S2");
		stationRepository.save(station2);
		
		Station station3 = new Station();
		station3.setStationName("S3");
		stationRepository.save(station3);

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

		// S1
		Track track1Station1 = new Track();
		track1Station1.setTrackNumber("track1Station1");
		track1Station1.setStation(station1);
		trackRepository.save(track1Station1);

		Track track2Station1 = new Track();
		track2Station1.setTrackNumber("track2Station1");
		track2Station1.setStation(station1);
		trackRepository.save(track2Station1);

		// S2
		Track track1Station2 = new Track();
		track1Station2.setTrackNumber("track1Station2");
		track1Station2.setStation(station2);
		trackRepository.save(track1Station2);
		
		Track track2Station2 = new Track();
		track2Station2.setTrackNumber("track2Station2");
		track2Station2.setStation(station2);
		trackRepository.save(track2Station2);
		
		// S3
		Track track1Station3 = new Track();
		track1Station3.setTrackNumber("track1Station3");
		track1Station3.setStation(station3);
		trackRepository.save(track1Station3);

		RailItemSequence seqLocos = new RailItemSequence();
		seqLocos.setSequenceIdentifier("seqLocos");
		seqLocos.setOrdinalPosition(0);
		railItemSequenceRepository.save(seqLocos);
		
		RailItemSequence seqTrack1Station1 = new RailItemSequence();
		seqTrack1Station1.setSequenceIdentifier("seqTrack1Station1");
		seqTrack1Station1.setOrdinalPosition(1);
		railItemSequenceRepository.save(seqTrack1Station1);
		
		RailItemSequence seqTrack2Station1 = new RailItemSequence();
		seqTrack2Station1.setSequenceIdentifier("seqTrack2Station1");
		seqTrack2Station1.setOrdinalPosition(0);
		railItemSequenceRepository.save(seqTrack2Station1);
		
		RailItemSequence seqTrack1Station2 = new RailItemSequence();
		seqTrack1Station2.setSequenceIdentifier("seqTrack1Station2");
		seqTrack1Station2.setOrdinalPosition(0);
		railItemSequenceRepository.save(seqTrack1Station2);

		putRailItemToTrack(locomotive1, track1Station1, seqLocos, 0);
		putRailItemToTrack(waggon123, track1Station1, seqTrack1Station1, 0);
		putRailItemToTrack(waggon234, track1Station1, seqTrack1Station1, 1);
		putRailItemToTrack(waggon345, track2Station1, seqTrack2Station1, 0);
		putRailItemToTrack(waggon456, track1Station2, seqTrack1Station2, 0);
		putRailItemToTrack(waggon567, track1Station2, seqTrack1Station2, 1);

		renderTracksAndWaggons("BEFORE");
	}

	private void putRailItemToTrack(RailItem railItem, Track track, RailItemSequence railItemSequence,
			int ordinalPosition) {

		System.out.println("putWaggonToTrack :: waggon = " + railItem.getIdentifier() + ", track = "
				+ track.getTrackNumber() + ", station = " + track.getStation().getStationName());

		railItemSequence.setTrack(track);
		railItemSequenceRepository.save(railItemSequence);

		RailItemSequenceMembership sequenceMembership = new RailItemSequenceMembership();
		sequenceMembership.setRailItem(railItem);
		sequenceMembership.setOrdinalPosition(ordinalPosition);
		sequenceMembership.setRailItemSequence(railItemSequence);
		railItemSequenceMembershipRepository.save(sequenceMembership);
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
}