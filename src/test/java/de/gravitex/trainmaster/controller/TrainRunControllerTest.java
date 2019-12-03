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
import de.gravitex.trainmaster.dto.StationInfoDTO;
import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.request.TrainRunDescriptor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TrainRunControllerTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 
	 * S1 ------------------------------------------------------ track1Station1 --->
	 * seqLocos{[L1]}seqTrack1Station1{[123][234]} track2Station1 --->
	 * seqTrack2Station1{[345]}
	 *
	 * S2 ------------------------------------------------------ track1Station2 --->
	 * seqTrack1Station2{[456][567]}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStationAndTracksWithTestData() throws Exception {

		// create test data
		mockMvc.perform(get(ServerMappings.TestData.CREATE));

		// get station data (before)
		StationsAndTracksAndWaggonsDTO satawBefore = getStationDataFromServer();

		assertTrackSequence(satawBefore, "S1", "track1Station1",
				"[track1Station1]::seqLocos{[L1]}seqTrack1Station1{[123][234]}");
		assertTrackSequence(satawBefore, "S1", "track2Station1", "[track2Station1]::seqTrack2Station1{[345]}");
		assertTrackSequence(satawBefore, "S2", "track1Station2", "[track1Station2]::seqTrack1Station2{[456][567]}");
		assertTrackSequence(satawBefore, "S2", "track2Station2", "[track2Station2]::#BLANK#");

		TrainRunDescriptor trainRunDescriptor = new TrainRunDescriptor();
		List<StationInfoDTO> stationInfoDTOs = new ArrayList<StationInfoDTO>();
		
		// S1
		StationInfoDTO stationInfo1 = new StationInfoDTO();
		stationInfo1.setStation("S1");
		stationInfo1.setExitTrack("TExitS1");
		stationInfoDTOs.add(stationInfo1);
		// S2
		StationInfoDTO stationInfo2 = new StationInfoDTO();
		stationInfo2.setStation("S2");
		stationInfo2.setEntryTrack("TEntryS2");
		stationInfo2.setExitTrack("TExitS2");
		stationInfoDTOs.add(stationInfo2);
		// S3
		StationInfoDTO stationInfo3 = new StationInfoDTO();
		stationInfo3.setStation("S3");
		stationInfo3.setEntryTrack("TEntryS3");
		stationInfoDTOs.add(stationInfo3);
		
		trainRunDescriptor.setStationInfoDTOs(stationInfoDTOs);

		// prepare train
		mockMvc.perform(MockMvcRequestBuilders.post(ServerMappings.TrainRun.PREPARE_TRAIN).content(new ObjectMapper().writeValueAsString(trainRunDescriptor))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(containsString("TRAIN_PREPARED")));

		// check something dumb
		mockMvc.perform(get(ServerMappings.TrainRun.DELETE_ME)).andExpect(content().string(containsString("DELETED")));

		// check we have a train

		// run train
		mockMvc.perform(get(ServerMappings.TrainRun.DEAPRT_TRAIN).param("trainNumber", "123")).andExpect(content().string(containsString("DEPARTED")));

		// get station data (after)
		StationsAndTracksAndWaggonsDTO satawAfter = getStationDataFromServer();
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