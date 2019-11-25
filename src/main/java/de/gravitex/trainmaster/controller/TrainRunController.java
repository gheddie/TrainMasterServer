package de.gravitex.trainmaster.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainRunController {
	
    private static final String template = "Hello, %s!";
    
    private final AtomicLong counter = new AtomicLong();
    
    /*
    @Autowired
    TrainRunRepository trainRunRepository;
    */

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		// System.out.println(trainRunRepository.findAll().size());
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}