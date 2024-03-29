package de.gravitex.trainmaster.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.dto.TrainRunSectionNodeDTO;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.exception.TrainRunException;
import de.gravitex.trainmaster.logic.CreateTrainRunSectionAction;
import de.gravitex.trainmaster.logic.TrainRunArrivalAction;
import de.gravitex.trainmaster.logic.TrainRunDepartureAction;
import de.gravitex.trainmaster.logic.TrainRunPrepareAction;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.repo.TrainRepository;
import de.gravitex.trainmaster.repo.TrainRunRepository;
import de.gravitex.trainmaster.repo.TrainRunSectionNodeRepository;
import de.gravitex.trainmaster.repo.TrainRunSectionRepository;
import de.gravitex.trainmaster.repo.WaggonRepository;
import de.gravitex.trainmaster.request.TrainRunDescriptor;
import de.gravitex.trainmaster.service.IRailService;

@RestController
public class TrainRunController implements ITrainRunController {

	@Autowired
	TrainRunRepository trainRunRepository;

	@Autowired
	TrainRepository trainRepository;

	@Autowired
	TrackRepository trackRepository;

	@Autowired
	TrainRunSectionRepository trainRunSectionRepository;

	@Autowired
	StationRepository stationRepository;

	@Autowired
	RailItemSequenceRepository railItemSequenceRepository;

	@Autowired
	WaggonRepository waggonRepository;
	
	@Autowired
	TrainRunSectionNodeRepository trainRunSectionNodeRepository;

	@Autowired
	IRailService trackService;

	@Transactional(rollbackOn = TrainRunException.class)
	@PostMapping(ServerMappings.TrainRun.TRAIN_PREPARATION)
	public ResponseEntity<String> trainPreparation(@RequestBody TrainRunDescriptor trainRunDescriptor) {

		TrainRunPrepareAction trainRunPrepareAction = new TrainRunPrepareAction();

		trainRunPrepareAction.setSequence(
				railItemSequenceRepository.findBySequenceIdentifier(trainRunDescriptor.getSequenceIdentifier()));

		trainRunPrepareAction.setTrainRunDescriptor(trainRunDescriptor);
		trainRunPrepareAction.execute();
		
		trainRunRepository.save(trainRunPrepareAction.getTrainRun());
		trainRepository.save(trainRunPrepareAction.getTrain());

		int index = 0;
		for (TrainRunSectionNodeDTO dto : trainRunDescriptor.getStationInfoDTOs()) {
			
			Station stationFrom = stationRepository.findByStationName(dto.getStationFrom());
			Station stationTo = stationRepository.findByStationName(dto.getStationTo());
			Track entryTrack = trackRepository.findByTrackNumber(dto.getEntryTrack());
			Track exitTrack = trackRepository.findByTrackNumber(dto.getExitTrack());
			
			CreateTrainRunSectionAction createTrainRunSectionAction = new CreateTrainRunSectionAction();
			createTrainRunSectionAction.setTrainRun(trainRunPrepareAction.getTrainRun());
			createTrainRunSectionAction.setTrainRunDescriptor(trainRunDescriptor);
			createTrainRunSectionAction.setStationFrom(stationFrom);
			createTrainRunSectionAction.setStationTo(stationTo);
			createTrainRunSectionAction.setEntryTrack(entryTrack);
			createTrainRunSectionAction.setExitTrack(exitTrack);
			createTrainRunSectionAction.setIndex(index);
			createTrainRunSectionAction.execute();
			
			trainRunSectionNodeRepository.save(createTrainRunSectionAction.getTrainRunSectionNodeFrom());
			trainRunSectionNodeRepository.save(createTrainRunSectionAction.getTrainRunSectionNodeTo());
			trainRunSectionRepository.save(createTrainRunSectionAction.getTrainRunSection());
			
			index++;
		}
		
		return new ResponseEntity<String>("TRAIN_PREPARED", HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(ServerMappings.TrainRun.TRAIN_DEAPRTURE)
	public ResponseEntity<String> trainDepature(@RequestParam(value = "trainNumber") String trainNumber) {

		TrainRunDepartureAction trainRunDepartureAction = new TrainRunDepartureAction();
		trainRunDepartureAction.setTrain(trainRepository.findByTrainNumber(trainNumber));
		trainRunDepartureAction.execute();

		return new ResponseEntity<String>("DEPARTED", HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.TRAIN_ARRIVAL)
	public ResponseEntity<String> trainArrival(@RequestParam(value = "trainNumber") String trainNumber) {
		
		TrainRunArrivalAction trainRunArrivalAction = new TrainRunArrivalAction();
		Train train = trainRepository.findByTrainNumber(trainNumber);
		trainRunArrivalAction.setTrain(train);
		trainRunArrivalAction.setSections(trainRunSectionRepository.findByTrainRun(train.getTrainRun()));
		trainRunArrivalAction.execute();
		
		trainRunRepository.save(train.getTrainRun());
		railItemSequenceRepository.save(train.getWaggonSequence());

		return new ResponseEntity<String>("ARRIVED", HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.STATION_DATA)
	public ResponseEntity<StationsAndTracksAndWaggonsDTO> stationData() {

		return new ResponseEntity<StationsAndTracksAndWaggonsDTO>(trackService.getStationsAndTracksAndWaggonsDTO(),
				HttpStatus.OK);
	}
}