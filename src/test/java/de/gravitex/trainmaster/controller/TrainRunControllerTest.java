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
import de.gravitex.trainmaster.dto.TrainRunSectionNodeDTO;
import de.gravitex.trainmaster.request.TrainRunDescriptor;
import de.gravitex.trainmaster.util.TrainRunTestUtil;

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
		mockMvc.perform(get(ServerMappings.TestData.CREATION));

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
}