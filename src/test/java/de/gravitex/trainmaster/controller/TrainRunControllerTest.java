package de.gravitex.trainmaster.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


// @ComponentScan(basePackages = "de.gravitex.trainmaster.repo")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TrainRunController.class)
// @SpringBootTest(classes={AppConfig.class})
public class TrainRunControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGreeting() throws Exception {
		mockMvc.perform(get("/greeting")).andExpect(content().string(containsString("Hello, World!")));
	}
}