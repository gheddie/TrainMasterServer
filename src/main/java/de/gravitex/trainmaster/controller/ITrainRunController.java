package de.gravitex.trainmaster.controller;

import org.springframework.http.ResponseEntity;

import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.request.TrainRunDescriptor;

public interface ITrainRunController {

	public ResponseEntity<StationsAndTracksAndWaggonsDTO> stationData();
	
	public ResponseEntity<String> trainPreparation(TrainRunDescriptor trainRunDescriptor);
	
	public ResponseEntity<String> trainDepature(String trainNumber);
}