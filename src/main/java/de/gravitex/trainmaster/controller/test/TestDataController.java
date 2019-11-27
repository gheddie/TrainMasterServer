package de.gravitex.trainmaster.controller.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.trainmaster.config.ServerMappings;

@RestController
public class TestDataController {

	@RequestMapping(ServerMappings.TestData.CREATE)
	public void create() {
		
	}
}