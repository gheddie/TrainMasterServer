package de.gravitex.trainmaster.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.repo.TrainRunRepository;

@RestController
public class TrainRunController {
	
    private static final String template = "Hello, %s!";
    
    private final AtomicLong counter = new AtomicLong();
    
    @Autowired
    TrainRunRepository trainRunRepository;
    
	@RequestMapping("/meeting")
	public Greeting meeting(@RequestParam(value = "name", defaultValue = "Meeting") String name) {
		trainRunRepository.save(new TrainRun());
		System.out.println("meeting : " + trainRunRepository.findAll().size());
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		trainRunRepository.save(new TrainRun());
		trainRunRepository.save(new TrainRun());
		System.out.println("greeting : " + trainRunRepository.findAll().size());
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}