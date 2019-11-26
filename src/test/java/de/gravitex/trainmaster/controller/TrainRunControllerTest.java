package de.gravitex.trainmaster.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import de.gravitex.trainmaster.config.ServerMappings;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TrainRunControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGreeting() throws Exception {
		mockMvc.perform(get(ServerMappings.MEETING)).andExpect(content().string(containsString("Hello, Meeting!")));
		mockMvc.perform(get(ServerMappings.GREETING)).andExpect(content().string(containsString("Hello, World!")));
		mockMvc.perform(get(ServerMappings.TRAIN).param("trackNumber", "track2Station1")).andExpect(content().string(containsString("123-456-789")));
	}
}