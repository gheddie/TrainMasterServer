package de.gravitex.trainmaster.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.logic.TrainRunSsequencePerformer;
import de.gravitex.trainmaster.util.ObjectHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TrainRunControllerTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 
	 * S1
	 * ------------------------------------------------------
	 * track1Station1 ---> seqLocos{[L1]}seqTrack1Station1{[123][234]} 
	 * track2Station1 ---> seqTrack2Station1{[345]}
	 *
	 * S2
	 * ------------------------------------------------------
	 * track1Station2 ---> seqTrack1Station2{[456][567]}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStationAndTracksWithTestData() throws Exception {

		mockMvc.perform(get(ServerMappings.TestData.CREATE));
		ResultActions resultActions = mockMvc
				.perform(get(ServerMappings.TrainRun.RUN_TRAIN).param("trainNumber", "ABC-DEF-GHI"));
		String json = resultActions.andReturn().getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		StationsAndTracksAndWaggonsDTO sataw = mapper.readValue(json,
				StationsAndTracksAndWaggonsDTO.class);
		
		assertTrackSequence(sataw, "S1", "track1Station1", "[track1Station1]::seqLocos{[L1]}seqTrack1Station1{[123][234]}");
		assertTrackSequence(sataw, "S1", "track2Station1", "[track2Station1]::seqTrack2Station1{[345]}");
		assertTrackSequence(sataw, "S2", "track1Station2", "[track1Station2]::seqTrack1Station2{[456][567]}");
		assertTrackSequence(sataw, "S2", "track2Station2", "[track2Station2]::#BLANK#");
	
		/*
		TrainRunSsequencePerformer performer = new TrainRunSsequencePerformer().withArguments(trackExitS1, locomotiveSequence, waggonSequenceAForExit, trackEntryS2, aTrain);
		performer.depart();
		*/
	}
	
	private void assertTrackSequence(StationsAndTracksAndWaggonsDTO stationAndTracksAndWaggons, String station, String track, String expectedTrackSequence) {
		String actualTrackWaggonsAsString = stationAndTracksAndWaggons.getTrackWaggonsAsString(station, track);
		Assert.isTrue(actualTrackWaggonsAsString.equals(expectedTrackSequence));
	}
}