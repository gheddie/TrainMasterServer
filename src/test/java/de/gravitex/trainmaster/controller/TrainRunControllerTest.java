package de.gravitex.trainmaster.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dlh.EntityHelper;
import de.gravitex.trainmaster.dlh.SimpleTrackRenderer;
import de.gravitex.trainmaster.dto.TrainRunSectionNodeDTO;
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
import de.gravitex.trainmaster.repo.TrainRepository;
import de.gravitex.trainmaster.request.TrainRunDescriptor;
import de.gravitex.trainmaster.util.TrainRunTestUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
// @DataJpaTest
public class TrainRunControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	TrainRepository trainRepository;

	@Autowired
	StationRepository stationRepository;

	@Autowired
	RailItemRepository railItemRepository;

	@Autowired
	TrackRepository trackRepository;

	@Autowired
	RailItemSequenceRepository railItemSequenceRepository;

	@Autowired
	RailItemSequenceMembershipRepository railItemSequenceMembershipRepository;

	/**
	 * 
	 * S1 ------------------------------------------------------
	 * track1Station1 ---> seqLocos{[L1]}seqTrack1Station1{[123][234]}
	 * track2Station1 ---> seqTrack2Station1{[345]}
	 *
	 * S2 ------------------------------------------------------
	 * track1Station2 ---> seqTrack1Station2{[456][567]}
	 * 
	 * S3 ------------------------------------------------------
	 * track1Station3 ---> #BLANK#
	 * 
	 * running train with number: TRAIN_TEST_NUMBER
	 * 
	 * S1 (exit) --> track1Station1
	 * 
	 * S2 (entry) --> track1Station2
	 * S2 (exit) --> track1Station2
	 * 
	 * S3 (entry) --> track1Station3
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStationAndTracksWithTestData() throws Exception {

		// create test data
		// mockMvc.perform(get(ServerMappings.TestData.CREATION));
		
		createTestData();

		// S1
		TrainRunTestUtil.assertTrackSequence("S1", "track1Station1",
				"[track1Station1]::seqLocos{[L1]}seqTrack1Station1{[123][234]}", mockMvc);
		TrainRunTestUtil.assertTrackSequence("S1", "track2Station1", "[track2Station1]::seqTrack2Station1{[345]}", mockMvc);
		
		// S2
		TrainRunTestUtil.assertTrackSequence("S2", "track1Station2", "[track1Station2]::seqTrack1Station2{[456][567]}", mockMvc);
		TrainRunTestUtil.assertTrackSequence("S2", "track2Station2", "[track2Station2]::#BLANK#", mockMvc);
		
		// S3
		TrainRunTestUtil.assertTrackSequence("S3", "track1Station3", "[track1Station3]::#BLANK#", mockMvc);

		TrainRunDescriptor trainRunDescriptor = new TrainRunDescriptor();
		trainRunDescriptor.setTrainNumber("TRAIN_TEST_NUMBER");
		trainRunDescriptor.setSequenceIdentifier("seqTrack1Station1");
		List<TrainRunSectionNodeDTO> stationInfoDTOs = new ArrayList<TrainRunSectionNodeDTO>();
		
		// S1 --> S2
		TrainRunSectionNodeDTO stationInfo1 = new TrainRunSectionNodeDTO();
		stationInfo1.setStationFrom("S1");
		stationInfo1.setStationTo("S2");
		stationInfo1.setExitTrack("track1Station1");
		stationInfo1.setEntryTrack("track1Station2");
		stationInfoDTOs.add(stationInfo1);
		// S2 --> S3
		TrainRunSectionNodeDTO stationInfo2 = new TrainRunSectionNodeDTO();
		stationInfo2.setStationFrom("S2");
		stationInfo2.setStationTo("S3");
		stationInfo2.setExitTrack("track1Station2");
		stationInfo2.setEntryTrack("track1Station3");
		stationInfoDTOs.add(stationInfo2);
		
		trainRunDescriptor.setStationInfoDTOs(stationInfoDTOs);

		// prepare train
		mockMvc.perform(MockMvcRequestBuilders.post(ServerMappings.TrainRun.TRAIN_PREPARATION).content(new ObjectMapper().writeValueAsString(trainRunDescriptor))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(containsString("TRAIN_PREPARED")));

		// depart train from 'S1' --> 'seqTrack1Station1' must have left track 'track1Station1'
		mockMvc.perform(get(ServerMappings.TrainRun.TRAIN_DEAPRTURE).param("trainNumber", "TRAIN_TEST_NUMBER")).andExpect(content().string(containsString("DEPARTED")));
		TrainRunTestUtil.assertTrackSequence("S1", "track1Station1", "[track1Station1]::seqLocos{[L1]}", mockMvc);
		
		// arrive train at 'S2' --> 'seqTrack1Station1' must appear on track 'track1Station2' 
		mockMvc.perform(get(ServerMappings.TrainRun.TRAIN_ARRIVAL).param("trainNumber", "TRAIN_TEST_NUMBER")).andExpect(content().string(containsString("ARRIVED")));
		TrainRunTestUtil.assertTrackSequence("S2", "track1Station2", "[track1Station2]::seqTrack1Station2{[456][567]}seqTrack1Station1{[123][234]}", mockMvc);
		
		// depart train from 'S2' --> 'seqTrack1Station1' must have left track 'track1Station2'
		mockMvc.perform(get(ServerMappings.TrainRun.TRAIN_DEAPRTURE).param("trainNumber", "TRAIN_TEST_NUMBER")).andExpect(content().string(containsString("DEPARTED")));
		TrainRunTestUtil.assertTrackSequence("S2", "track1Station2", "[track1Station2]::seqTrack1Station2{[456][567]}", mockMvc);
		
		// arrive train at 'S3' --> 'seqTrack1Station1' must appear on track 'track1Station3'
		mockMvc.perform(get(ServerMappings.TrainRun.TRAIN_ARRIVAL).param("trainNumber", "TRAIN_TEST_NUMBER")).andExpect(content().string(containsString("ARRIVED")));
		TrainRunTestUtil.assertTrackSequence("S3", "track1Station3", "[track1Station3]::seqTrack1Station1{[123][234]}", mockMvc);
	}
	
	@Test
	public void testTrainDepartureWithoutBrakeChecks() throws Exception {

		/*
		System.out.println("TR : " + trainRepository);
		mockMvc.perform(get(ServerMappings.TestData.CHECK));
		*/
	}
	
	// ---
	
	private void createTestData() {
		
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