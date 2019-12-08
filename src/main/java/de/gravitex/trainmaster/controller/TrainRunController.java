package de.gravitex.trainmaster.controller;

import java.util.List;

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
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.enumeration.TrainState;
import de.gravitex.trainmaster.entity.trainrun.TrainRun;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSection;
import de.gravitex.trainmaster.exception.TrainRunException;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.repo.TrainRepository;
import de.gravitex.trainmaster.repo.TrainRunRepository;
import de.gravitex.trainmaster.repo.TrainRunSectionRepository;
import de.gravitex.trainmaster.repo.WaggonRepository;
import de.gravitex.trainmaster.request.TrainRunDescriptor;
import de.gravitex.trainmaster.service.ITrackService;

@RestController
public class TrainRunController implements ITrainRunController {

	@Autowired
	private TrainRunRepository trainRunRepository;

	@Autowired
	private TrainRepository trainRepository;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private TrainRunSectionRepository trainRunSectionRepository;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private RailItemSequenceRepository railItemSequenceRepository;

	@Autowired
	WaggonRepository waggonRepository;

	@Autowired
	ITrackService trackService;

	@Transactional(rollbackOn = TrainRunException.class)
	@PostMapping(ServerMappings.TrainRun.TRAIN_PREPARATION)
	public ResponseEntity<String> trainPreparation(@RequestBody TrainRunDescriptor trainRunDescriptor) {

		TrainRun trainRun = new TrainRun();
		trainRunRepository.save(trainRun);

		Train train = new Train();
		train.setTrainState(TrainState.PREPARED);
		train.setTrainNumber(trainRunDescriptor.getTrainNumber());
		RailItemSequence sequence = railItemSequenceRepository
				.findBySequenceIdentifier(trainRunDescriptor.getSequenceIdentifier());
		train.setWaggonSequence(sequence);
		train.setTrainRun(trainRun);
		trainRepository.save(train);

		int index = 0;
		for (TrainRunSectionNodeDTO dto : trainRunDescriptor.getStationInfoDTOs()) {
			trackService.createTrainRunSection(trainRun, stationRepository.findByStationName(dto.getStationFrom()),
					stationRepository.findByStationName(dto.getStationTo()),
					trackRepository.findByTrackNumber(dto.getEntryTrack()),
					trackRepository.findByTrackNumber(dto.getExitTrack()), index,
					trainRunDescriptor.getStationInfoDTOs().size());
			index++;
		}

		return new ResponseEntity<String>("TRAIN_PREPARED", HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.TRAIN_DEAPRTURE)
	public ResponseEntity<String> trainDepature(@RequestParam(value = "trainNumber") String trainNumber) {

		Train train = trainRepository.findByTrainNumber(trainNumber);

		// remove train waggon sequnce from track
		train.getWaggonSequence().setTrack(null);

		return new ResponseEntity<String>("DEPARTED", HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.TRAIN_ARRIVAL)
	public ResponseEntity<String> trainArrival(@RequestParam(value = "trainNumber") String trainNumber) {

		Train train = trainRepository.findByTrainNumber(trainNumber);

		TrainRun trainRun = train.getTrainRun();

		int actualTrainRunSectionIndex = trainRun.getActualTrainRunSectionIndex();
		List<TrainRunSection> sections = trainRunSectionRepository.findByTrainRun(trainRun);
		TrainRunSection trainRunSection = sections.get(actualTrainRunSectionIndex);

		// increase train run index
		actualTrainRunSectionIndex++;
		trainRun.setActualTrainRunSectionIndex(actualTrainRunSectionIndex);
		trainRunRepository.save(trainRun);

		// add waggons to entry track
		Track entryTrack = trainRunSection.getNodeTo().getEntryTrack();
		List<RailItemSequence> actualSequences = railItemSequenceRepository.findByTrack(entryTrack);
		RailItemSequence sequence = train.getWaggonSequence();

		sequence.setTrack(entryTrack);
		railItemSequenceRepository.save(sequence);

		// add entry train sequence to entry track
		actualSequences.add(sequence);

		return new ResponseEntity<String>("ARRIVED", HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.STATION_DATA)
	public ResponseEntity<StationsAndTracksAndWaggonsDTO> stationData() {

		return new ResponseEntity<StationsAndTracksAndWaggonsDTO>(trackService.getStationsAndTracksAndWaggonsDTO(),
				HttpStatus.OK);
	}
}