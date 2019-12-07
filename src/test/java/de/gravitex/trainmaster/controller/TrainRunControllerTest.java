package de.gravitex.trainmaster.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dto.TrainRunSectionNodeDTO;
import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.request.TrainRunDescriptor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TrainRunControllerTest {

	@Autowired
	private MockMvc mockMvc;

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
		mockMvc.perform(get(ServerMappings.TestData.CREATE));

		// get station data (before)
		StationsAndTracksAndWaggonsDTO stationInfo = getStationDataFromServer();

		// S1
		assertTrackSequence(stationInfo, "S1", "track1Station1",
				"[track1Station1]::seqLocos{[L1]}seqTrack1Station1{[123][234]}");
		assertTrackSequence(stationInfo, "S1", "track2Station1", "[track2Station1]::seqTrack2Station1{[345]}");
		
		// S2
		assertTrackSequence(stationInfo, "S2", "track1Station2", "[track1Station2]::seqTrack1Station2{[456][567]}");
		assertTrackSequence(stationInfo, "S2", "track2Station2", "[track2Station2]::#BLANK#");
		
		// S3
		assertTrackSequence(stationInfo, "S3", "track1Station3", "[track1Station3]::#BLANK#");

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
		stationInfo2.setEntryTrack("track1Station2");
		stationInfo2.setExitTrack("track1Station3");
		stationInfoDTOs.add(stationInfo2);
		
		trainRunDescriptor.setStationInfoDTOs(stationInfoDTOs);

		// prepare train
		mockMvc.perform(MockMvcRequestBuilders.post(ServerMappings.TrainRun.PREPARE_TRAIN).content(new ObjectMapper().writeValueAsString(trainRunDescriptor))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(containsString("TRAIN_PREPARED")));

		// check something dumb
		mockMvc.perform(get(ServerMappings.TrainRun.DELETE_ME)).andExpect(content().string(containsString("DELETED")));

		// check we have a train

		// depart train
		mockMvc.perform(get(ServerMappings.TrainRun.DEAPRT_TRAIN).param("trainNumber", "TRAIN_TEST_NUMBER")).andExpect(content().string(containsString("DEPARTED")));

		// 'seqTrack1Station1' must have left track 'track1Station1'
		assertTrackSequence(getStationDataFromServer(), "S1", "track1Station1", "[track1Station1]::seqLocos{[L1]}");
		
		// arrive train
		mockMvc.perform(get(ServerMappings.TrainRun.ARRIVE_TRAIN).param("trainNumber", "TRAIN_TEST_NUMBER")).andExpect(content().string(containsString("ARRIVED")));
		
		// 'seqTrack1Station1' must appear on track 'track1Station2'
		assertTrackSequence(getStationDataFromServer(), "S2", "track1Station2", "[track1Station2]::seqTrack1Station2{[456][567]}seqTrack1Station1{[123][234]}");
	}

	private StationsAndTracksAndWaggonsDTO getStationDataFromServer()
			throws Exception, UnsupportedEncodingException, JsonProcessingException, JsonMappingException {

		ResultActions resultActions = mockMvc.perform(get(ServerMappings.TrainRun.STATION_DATA));
		String json = resultActions.andReturn().getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		StationsAndTracksAndWaggonsDTO sataw = mapper.readValue(json, StationsAndTracksAndWaggonsDTO.class);
		return sataw;
	}

	private void assertTrackSequence(StationsAndTracksAndWaggonsDTO stationAndTracksAndWaggons, String station,
			String track, String expectedTrackSequence) {
		String actualTrackWaggonsAsString = stationAndTracksAndWaggons.getTrackWaggonsAsString(station, track);
		Assert.isTrue(actualTrackWaggonsAsString.equals(expectedTrackSequence));
	}
}