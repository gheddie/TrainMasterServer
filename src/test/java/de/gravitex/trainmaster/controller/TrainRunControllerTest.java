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

import com.fasterxml.jackson.databind.ObjectMapper;

import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dto.StationAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.util.ObjectHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TrainRunControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testStationAndTracks() throws Exception {
		/*
		ResultActions result = mockMvc.perform(get(ServerMappings.TRACKPOPULATION).param("stationName", "ssssssssssss"));
		result.andExpect(content().string(containsString("Hello, Meeting!")));
		*/
		ResultActions result = mockMvc.perform(get(ServerMappings.TRACKPOPULATION).param("stationName", "ssssssssssss"));
		String json = result.andReturn().getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		StationAndTracksAndWaggonsDTO stationAndTracksAndWaggons = mapper.readValue(json, StationAndTracksAndWaggonsDTO.class);
		ObjectHelper.renderStation(stationAndTracksAndWaggons);
	}

	@Test
	public void testGreeting() throws Exception {
		mockMvc.perform(get(ServerMappings.MEETING)).andExpect(content().string(containsString("Hello, Meeting!")));
		mockMvc.perform(get(ServerMappings.GREETING)).andExpect(content().string(containsString("Hello, World!")));
		mockMvc.perform(get(ServerMappings.TRAIN).param("trackNumber", "track2Station1")).andExpect(content().string(containsString("123-456-789")));
	}
}