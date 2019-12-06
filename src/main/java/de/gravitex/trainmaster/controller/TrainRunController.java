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

import de.gravitex.trainmaster.common.DataGrid;
import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dto.StationInfoDTO;
import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.TrainRunSectionNode;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.entity.TrainRunSection;
import de.gravitex.trainmaster.entity.enumeration.TrainState;
import de.gravitex.trainmaster.exception.TrainRunException;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.StationInfoRepository;
import de.gravitex.trainmaster.repo.StationRepository;
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
	private TrainRunSectionRepository trainRunSectionRepository;

	@Autowired
	private StationInfoRepository stationInfoRepository;

	@Autowired
	private StationRepository stationRepository;
	
	@Autowired
	private RailItemSequenceRepository railItemSequenceRepository;

	@Autowired
	WaggonRepository waggonRepository;

	@Autowired
	ITrackService trackService;

	@Transactional(rollbackOn = TrainRunException.class)
	@PostMapping(ServerMappings.TrainRun.PREPARE_TRAIN)
	public ResponseEntity<String> prepareTrain(@RequestBody TrainRunDescriptor trainRunDescriptor) {
		
		TrainRun trainRun = new TrainRun();
		trainRunRepository.save(trainRun);
		
		Train train = new Train();
		train.setTrainState(TrainState.PREPARED);
		train.setTrainNumber(trainRunDescriptor.getTrainNumber());
		RailItemSequence sequence = railItemSequenceRepository.findBySequenceIdentifier(trainRunDescriptor.getSequenceIdentifier());
		train.setWaggonSequence(sequence);
		train.setTrainRun(trainRun);
		trainRepository.save(train);

		for (int index = 0; index < trainRunDescriptor.getStationInfoDTOs().size() - 1; index++) {

			StationInfoDTO stationInfoFrom = trainRunDescriptor.getStationInfoDTOs().get(index);
			StationInfoDTO stationInfoTo = trainRunDescriptor.getStationInfoDTOs().get(index + 1);

			createTrainRunSection(trainRun, stationRepository.findByStationName(stationInfoFrom.getStation()),
					stationRepository.findByStationName(stationInfoTo.getStation()), index);
		}
		return new ResponseEntity<String>("TRAIN_PREPARED", HttpStatus.OK);
	}

	private void createTrainRunSection(TrainRun trainRun, Station stationFrom, Station stationTo, int sectionIndex) {

		TrainRunSectionNode infoFrom = new TrainRunSectionNode();
		infoFrom.setStation(stationFrom);
		stationInfoRepository.save(infoFrom);

		TrainRunSectionNode infoTo = new TrainRunSectionNode();
		infoTo.setStation(stationTo);
		stationInfoRepository.save(infoTo);

		TrainRunSection section = new TrainRunSection();
		section.setStationFrom(infoFrom);
		section.setStationTo(infoTo);
		section.setTrainRun(trainRun);
		section.setSectionIndex(sectionIndex);
		
		/*
		trainRun.getTrainRunSections().add(section);
		trainRunRepository.save(trainRun);
		*/
		
		trainRunSectionRepository.save(section);

		// ---

		/*
		 * Station s1 = new Station(); s1.setStationName("s1");
		 * stationRepository.save(s1); StationInfo stationFrom = new StationInfo();
		 * stationFrom.setStation(s1); stationInfoRepository.save(stationFrom);
		 * 
		 * Station s2 = new Station(); s2.setStationName("s2");
		 * stationRepository.save(s2); StationInfo stationTo = new StationInfo();
		 * stationTo.setStation(s2); stationInfoRepository.save(stationTo);
		 * 
		 * TrainRunSection section = new TrainRunSection();
		 * section.setStationFrom(stationFrom); section.setStationTo(stationTo);
		 * section.setTrainRun(trainRun); trainRunSectionRepository.save(section);
		 */
	}

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.DELETE_ME)
	public ResponseEntity<String> deleteMe() {

		new DataGrid<TrainRunSection>().withConfiguration(null).print(trainRunSectionRepository.findAll());
		return new ResponseEntity<String>("DELETED", HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.DEAPRT_TRAIN)
	public ResponseEntity<String> departTrain(@RequestParam(value = "trainNumber") String trainNumber) {
		
		Train train = trainRepository.findByTrainNumber(trainNumber);
		
		// remove train waggon sequnce from track
		train.getWaggonSequence().setTrack(null);

		return new ResponseEntity<String>("DEPARTED", HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(ServerMappings.TrainRun.ARRIVE_TRAIN)
	public ResponseEntity<String> arriveTrain(@RequestParam(value = "trainNumber") String trainNumber) {
		
		Train train = trainRepository.findByTrainNumber(trainNumber);
		int actualTrainRunSectionIndex = train.getTrainRun().getActualTrainRunSectionIndex();
		TrainRunSection trainRunSection = trainRunSectionRepository.findByTrainRun(train.getTrainRun()).get(actualTrainRunSectionIndex);
		Track entryTrack = trainRunSection.getStationTo().getEntryTrack();
		
		return new ResponseEntity<String>("ARRIVED", HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.STATION_DATA)
	public ResponseEntity<StationsAndTracksAndWaggonsDTO> stationData() {

		/*
		 * new DataGrid<RailItemSequenceMembership>().withConfiguration(
		 * DataGridConfiguration.fromValues(null))
		 * .print(railItemSequenceMembershipRepository.findAll());
		 */

		return new ResponseEntity<StationsAndTracksAndWaggonsDTO>(trackService.getStationsAndTracksAndWaggonsDTO(),
				HttpStatus.OK);
	}
}