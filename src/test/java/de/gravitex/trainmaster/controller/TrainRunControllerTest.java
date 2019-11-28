package de.gravitex.trainmaster.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
	 * track1Station1 ---> [L1][123][234] 
	 * track2Station1 ---> [345]
	 *
	 * S2
	 * ------------------------------------------------------
	 * track1Station2 ---> [456][567]
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
		StationsAndTracksAndWaggonsDTO stationAndTracksAndWaggons = mapper.readValue(json,
				StationsAndTracksAndWaggonsDTO.class);
		// resultActions.andExpect(content().string(equalTo("OK")));
		String trackWaggonsAsString = stationAndTracksAndWaggons.getTrackWaggonsAsString("S2", "track1Station2");
		Assert.isTrue(trackWaggonsAsString.equals("[track1Station1] --> [L1::0/0][123::1/0][234::1/1]"));
	}

	@Test
	public void testStationAndTracks() throws Exception {
		
		/*
		 * ResultActions result =
		 * mockMvc.perform(get(ServerMappings.TRACKPOPULATION).param("stationName",
		 * "ssssssssssss"));
		 * result.andExpect(content().string(containsString("Hello, Meeting!")));
		 */
		
		ResultActions result = mockMvc
				.perform(get(ServerMappings.TrainRun.TRACKPOPULATION).param("stationName", "ssssssssssss"));
		String json = result.andReturn().getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		StationsAndTracksAndWaggonsDTO stationAndTracksAndWaggons = mapper.readValue(json,
				StationsAndTracksAndWaggonsDTO.class);
		ObjectHelper.renderStation(stationAndTracksAndWaggons);
	}

	/*
	@Test
	public void testGreeting() throws Exception {
		mockMvc.perform(get(ServerMappings.TrainRun.MEETING))
				.andExpect(content().string(containsString("Hello, Meeting!")));
		mockMvc.perform(get(ServerMappings.TrainRun.GREETING))
				.andExpect(content().string(containsString("Hello, World!")));
		mockMvc.perform(get(ServerMappings.TrainRun.TRAIN).param("trackNumber", "track2Station1"))
				.andExpect(content().string(containsString("123-456-789")));
	}
	*/
}