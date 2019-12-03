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
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.StationInfo;
import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.entity.TrainRunSection;
import de.gravitex.trainmaster.exception.TrainRunException;
import de.gravitex.trainmaster.repo.StationInfoRepository;
import de.gravitex.trainmaster.repo.StationRepository;
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
	private TrainRunSectionRepository trainRunSectionRepository;

	@Autowired
	private StationInfoRepository stationInfoRepository;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	WaggonRepository waggonRepository;

	@Autowired
	ITrackService trackService;

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.TRAIN)
	public ResponseEntity<String> train(@RequestParam(value = "trackNumber") String trackNumber) {

		/*
		 * Station station1 = new Station("S1"); stationRepository.save(station1);
		 * 
		 * Station station2 = new Station("S2"); stationRepository.save(station2);
		 * 
		 * Locomotive locomotive1 = EntityHelper.makeLocomotive("L1");
		 * railItemRepository.save(locomotive1);
		 * 
		 * Waggon waggon123 = EntityHelper.makeWaggon("123");
		 * railItemRepository.save(waggon123);
		 * 
		 * Waggon waggon234 = EntityHelper.makeWaggon("234");
		 * railItemRepository.save(waggon234);
		 * 
		 * Waggon waggon345 = EntityHelper.makeWaggon("345");
		 * railItemRepository.save(waggon345);
		 * 
		 * Waggon waggon456 = EntityHelper.makeWaggon("456");
		 * railItemRepository.save(waggon456);
		 * 
		 * Waggon waggon567 = EntityHelper.makeWaggon("567");
		 * railItemRepository.save(waggon567);
		 * 
		 * Track track1Station1 = new Track("track1Station1");
		 * track1Station1.setStation(station1); trackRepository.save(track1Station1);
		 * 
		 * Track track2Station1 = new Track("track2Station1");
		 * track2Station1.setStation(station1); trackRepository.save(track2Station1);
		 * 
		 * Track track1Station2 = new Track("track1Station2");
		 * track1Station2.setStation(station2); trackRepository.save(track1Station2);
		 * 
		 * RailItemSequence seqLocos = new RailItemSequence(0);
		 * railItemSequenceRepository.save(seqLocos); RailItemSequence seqTrack1Station1
		 * = new RailItemSequence(1);
		 * railItemSequenceRepository.save(seqTrack1Station1); RailItemSequence
		 * seqTrack2Station1 = new RailItemSequence(0);
		 * railItemSequenceRepository.save(seqTrack2Station1); RailItemSequence
		 * seqTrack1Station2 = new RailItemSequence(0);
		 * railItemSequenceRepository.save(seqTrack1Station2);
		 * 
		 * putRailItemToTrack(locomotive1, track1Station1, seqLocos, 0);
		 * putRailItemToTrack(waggon123, track1Station1, seqTrack1Station1, 0);
		 * putRailItemToTrack(waggon234, track1Station1, seqTrack1Station1, 1);
		 * putRailItemToTrack(waggon345, track2Station1, seqTrack2Station1, 0);
		 * putRailItemToTrack(waggon456, track1Station2, seqTrack1Station2, 0);
		 * putRailItemToTrack(waggon567, track1Station2, seqTrack1Station2, 1);
		 * 
		 * renderTracksAndWaggons("BEFORE");
		 * 
		 * // create train with a train run Station 1 -> Station 2 StationInfo
		 * stationInfo1 = new StationInfo(station1, null, track1Station1);
		 * stationInfoRepository.save(stationInfo1); StationInfo stationInfo2 = new
		 * StationInfo(station2, track1Station2, null);
		 * stationInfoRepository.save(stationInfo2); TrainRunSection sec1 = new
		 * TrainRunSection(stationInfo1, stationInfo2);
		 * trainRunSectionRepository.save(sec1); Train train = new Train(); TrainRun
		 * trainRun = new TrainRun(); trainRunRepository.save(trainRun);
		 * train.setTrainRun(trainRun); trainRun.getTrainRunSections().add(sec1);
		 * trainRepository.save(train);
		 */

		// ---------------------------------------------

		/*
		 * // run train TrainRunner trainRunner = new TrainRunner();
		 * trainRunner.withArguments(track1Station1, seqLocos, seqTrack1Station1,
		 * track1Station2, train); trainRunner.depart();
		 * renderTracksAndWaggons("AFTER DEPART"); String trackSequence =
		 * productService.getTrackSequenceAsString(trackNumber); return new
		 * ResponseEntity<String>(trackSequence, HttpStatus.OK);
		 */

		return null;
	}

	@Transactional(rollbackOn = TrainRunException.class)
	@PostMapping(ServerMappings.TrainRun.PREPARE_TRAIN)
	public ResponseEntity<String> prepareTrain(@RequestBody TrainRunDescriptor trainRunDescriptor) {

		TrainRun trainRun = new TrainRun();
		trainRunRepository.save(trainRun);

		for (int index = 0; index < trainRunDescriptor.getStationInfoDTOs().size() - 1; index++) {

			StationInfoDTO stationInfoFrom = trainRunDescriptor.getStationInfoDTOs().get(index);
			StationInfoDTO stationInfoTo = trainRunDescriptor.getStationInfoDTOs().get(index + 1);

			/*
			 * System.out.println(stationInfoFrom.getStation() + " --> " +
			 * stationInfoTo.getStation());
			 */

			createTrainRunSection(trainRun, stationRepository.findByStationName(stationInfoFrom.getStation()),
					stationRepository.findByStationName(stationInfoTo.getStation()), index);
		}

		// trainRun.getTrainRunSections().add(section);

		return new ResponseEntity<String>("TRAIN_PREPARED", HttpStatus.OK);
	}

	private void createTrainRunSection(TrainRun trainRun, Station stationFrom, Station stationTo, int sectionIndex) {

		StationInfo infoFrom = new StationInfo();
		infoFrom.setStation(stationFrom);
		stationInfoRepository.save(infoFrom);

		StationInfo infoTo = new StationInfo();
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

		return new ResponseEntity<String>("DEPARTED", HttpStatus.OK);

		// ---

		/*
		 * Train train = trainRepository.findByTrainNumber("ABC-DEF-GHI");
		 * 
		 * Track exitTrack = null; RailItemSequence locomotiveSequence = null;
		 * RailItemSequence waggonSequenceForExit = null; Track aEntryTrack = null;
		 * 
		 * TrainRunSequencePerformer performer = new
		 * TrainRunSequencePerformer().withArguments(exitTrack, locomotiveSequence,
		 * waggonSequenceForExit, aEntryTrack, train);
		 * 
		 * performer.depart();
		 * 
		 * return null;
		 */
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