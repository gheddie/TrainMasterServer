package de.gravitex.trainmaster.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.gravitex.trainmaster.dto.RailItemDTO;
import de.gravitex.trainmaster.dto.RailItemSequenceDTO;
import de.gravitex.trainmaster.dto.StationDTO;
import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.dto.TrackDTO;
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.trainrun.FinalTrainRunSection;
import de.gravitex.trainmaster.entity.trainrun.InitialTrainRunSection;
import de.gravitex.trainmaster.entity.trainrun.IntermediateTrainRunSection;
import de.gravitex.trainmaster.entity.trainrun.TrainRun;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSection;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSectionArrivalNode;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSectionDepartureNode;
import de.gravitex.trainmaster.repo.RailItemRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceMembershipRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.TrainRunSectionNodeRepository;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.repo.TrainRunRepository;
import de.gravitex.trainmaster.repo.TrainRunSectionRepository;

@Component
public class TrackService implements ITrackService {

	@Autowired
	TrainRunRepository trainRunRepository;
	
	@Autowired
	TrainRunSectionRepository trainRunSectionRepository;

	@Autowired
	TrackRepository trackRepository;
	
	@Autowired
	StationRepository stationRepository;
	
	@Autowired
	RailItemSequenceMembershipRepository railItemSequenceMembershipRepository;
	
	@Autowired
	RailItemSequenceRepository railItemSequenceRepository;
	
	@Autowired
	RailItemRepository railItemRepository;
	
	@Autowired
	TrainRunSectionNodeRepository trainRunSectionNodeRepository;

	@Override
	public String getTrackSequenceAsString(String trackNumber) {
		Track track = trackRepository.findByTrackNumber(trackNumber);
		List<RailItemSequenceMembership> sequenceMemberships = null;
		String result = "";
		for (RailItemSequence railItemSequence : railItemSequenceRepository.findByTrack(track)) {
			sequenceMemberships = railItemSequenceMembershipRepository.findByRailItemSequence(railItemSequence);
			for (RailItemSequenceMembership sequenceMembership : sequenceMemberships) {
				result += sequenceMembership.getRailItem().getIdentifier() + "#";
			}
		}
		return result;
	}

	@Override
	public StationsAndTracksAndWaggonsDTO getStationsAndTracksAndWaggonsDTO() {
		
		StationsAndTracksAndWaggonsDTO result = new StationsAndTracksAndWaggonsDTO();
		
		StationDTO stationDTO = null;
		TrackDTO trackDTO = null;
		RailItemSequenceDTO railItemSequenceDTO = null;
		RailItemDTO railItemDTO = null;
		for (Station station : stationRepository.findAll()) {
			stationDTO = new StationDTO();
			stationDTO.fillValues(station);
			result.addStation(stationDTO);
			for (Track track : trackRepository.findByStation(station)) {
				trackDTO = new TrackDTO();
				trackDTO.fillValues(track);
				result.addTrack(stationDTO, trackDTO);
				for (RailItemSequence railItemSequence : railItemSequenceRepository.findByTrackOrderedByOrdinalPosition(track)) {
					railItemSequenceDTO = new RailItemSequenceDTO();
					railItemSequenceDTO.fillValues(railItemSequence);
					result.addRailItemSequence(stationDTO, trackDTO, railItemSequenceDTO);
					for (RailItemSequenceMembership railItemSequenceMembership : railItemSequenceMembershipRepository.findByRailItemSequence(railItemSequence)) {
						railItemDTO = new RailItemDTO();
						railItemDTO.fillValues(railItemSequenceMembership.getRailItem());
						result.addRailItem(stationDTO, trackDTO, railItemSequenceDTO, railItemDTO);	
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public void createTrainRunSection(TrainRun trainRun, Station stationFrom, Station stationTo, Track entryTrack,
			Track exitTrack, int sectionIndex, int totalStationCount) {
		
		TrainRunSectionDepartureNode trainRunSectionNodeFrom = new TrainRunSectionDepartureNode();
		trainRunSectionNodeFrom.setStationFrom(stationFrom);
		trainRunSectionNodeFrom.setExitTrack(exitTrack);
		trainRunSectionNodeRepository.save(trainRunSectionNodeFrom);

		TrainRunSectionArrivalNode trainRunSectionNodeTo = new TrainRunSectionArrivalNode();
		trainRunSectionNodeTo.setStationTo(stationTo);
		trainRunSectionNodeTo.setEntryTrack(entryTrack);
		trainRunSectionNodeRepository.save(trainRunSectionNodeTo);

		TrainRunSection trainRunSection = null;

		if (sectionIndex == 0) {
			trainRunSection = new InitialTrainRunSection();
		} else if (sectionIndex == totalStationCount) {
			trainRunSection = new IntermediateTrainRunSection();
		} else {
			trainRunSection = new FinalTrainRunSection();
		}

		trainRunSection.setNodeFrom(trainRunSectionNodeFrom);
		trainRunSection.setNodeTo(trainRunSectionNodeTo);
		
		trainRunSection.setTrainRun(trainRun);
		trainRunSection.setSectionIndex(sectionIndex);

		trainRunSectionRepository.save(trainRunSection);
		
	}
}