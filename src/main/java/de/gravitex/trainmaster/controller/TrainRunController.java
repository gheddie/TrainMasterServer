package de.gravitex.trainmaster.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.trainmaster.common.DataGrid;
import de.gravitex.trainmaster.common.DataGridConfiguration;
import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dlh.EntityHelper;
import de.gravitex.trainmaster.dlh.SimpleTrackRenderer;
import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.dto.test.GreetingDTO;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.TrainRun;
import de.gravitex.trainmaster.repo.RailItemRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceMembershipRepository;
import de.gravitex.trainmaster.repo.StationInfoRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.repo.TrainRepository;
import de.gravitex.trainmaster.repo.TrainRunRepository;
import de.gravitex.trainmaster.repo.TrainRunSectionRepository;
import de.gravitex.trainmaster.repo.WaggonRepository;
import de.gravitex.trainmaster.service.ITrackService;

@RestController
public class TrainRunController {

	private static final String template = "Hello, %s!";

	private final AtomicLong counter = new AtomicLong();

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private RailItemRepository railItemRepository;
	
	@Autowired
	private RailItemSequenceMembershipRepository railItemSequenceMembershipRepository;

	@Autowired
	private TrainRepository trainRepository;

	@Autowired
	private TrainRunRepository trainRunRepository;

	@Autowired
	private TrainRunSectionRepository trainRunSectionRepository;

	@Autowired
	private StationInfoRepository stationInfoRepository;

	@Autowired
	WaggonRepository waggonRepository;

	@Autowired
	ITrackService trackService;

	@RequestMapping(ServerMappings.TrainRun.MEETING)
	public GreetingDTO meeting(@RequestParam(value = "name", defaultValue = "Meeting") String name) {
		trainRunRepository.save(new TrainRun());
		waggonRepository.save(EntityHelper.makeWaggon("123A"));
		System.out.println("meeting [train runs] : " + trainRunRepository.findAll().size());
		System.out.println("meeting [waggons] : " + waggonRepository.findAll().size());
		return new GreetingDTO(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping(ServerMappings.TrainRun.GREETING)
	public GreetingDTO greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		trainRunRepository.save(new TrainRun());
		trainRunRepository.save(new TrainRun());
		waggonRepository.save(EntityHelper.makeWaggon("234A"));
		waggonRepository.save(EntityHelper.makeWaggon("345A"));
		System.out.println("meeting [train runs] : " + trainRunRepository.findAll().size());
		System.out.println("meeting [waggons] : " + waggonRepository.findAll().size());
		return new GreetingDTO(counter.incrementAndGet(), String.format(template, name));
	}

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

	@Transactional
	@RequestMapping(ServerMappings.TrainRun.RUN_TRAIN)
	public ResponseEntity<StationsAndTracksAndWaggonsDTO> runTrain(
			@RequestParam(value = "trainNumber") String trainNumber) {

		// ---

		/*
		List<String> headers = new ArrayList<String>();
		headers.add("waggonNumber");
		*/
		new DataGrid<RailItemSequenceMembership>().withConfiguration(DataGridConfiguration.fromValues(null))
				.print(railItemSequenceMembershipRepository.findAll());

		// ---

		Train train = trainRepository.findByTrainNumber(trainNumber);

		/*
		 * if (train == null) { return new
		 * ResponseEntity<StationsAndTracksAndWaggonsDTO>(new
		 * StationsAndTracksAndWaggonsDTO(), HttpStatus.INTERNAL_SERVER_ERROR); }
		 * 
		 * if (train.getLocomotives() == null) { return new
		 * ResponseEntity<StationsAndTracksAndWaggonsDTO>(new
		 * StationsAndTracksAndWaggonsDTO(), HttpStatus.INTERNAL_SERVER_ERROR); }
		 */

		System.out.println("running train: " + trainNumber);

		// run train
		/*
		 * TrainRunner trainRunner = new TrainRunner();
		 * trainRunner.withArguments(track1Station1, seqLocos, seqTrack1Station1,
		 * track1Station2, train); trainRunner.depart();
		 * renderTracksAndWaggons("AFTER DEPART"); String trackSequence =
		 * productService.getTrackSequenceAsString(trackNumber); return new
		 * ResponseEntity<String>(trackSequence, HttpStatus.OK);
		 */

		StationsAndTracksAndWaggonsDTO result = trackService.getStationsAndTracksAndWaggonsDTO();
		return new ResponseEntity<StationsAndTracksAndWaggonsDTO>(result, HttpStatus.OK);

		// return ServerResult.fromValues();
	}

	private String renderTracksAndWaggons(String description) {

		List<RailItemSequenceMembership> railItems = null;
		SimpleTrackRenderer simpleTrackRenderer = new SimpleTrackRenderer();
		simpleTrackRenderer.setDescription(description);
		for (Track t : trackRepository.findAll()) {
			railItems = railItemRepository.findByTrack(t);
			simpleTrackRenderer.putTrackWaggons(t, railItems);
		}
		return simpleTrackRenderer.render();
	}
}