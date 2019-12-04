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
import de.gravitex.trainmaster.repo.RailItemRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceMembershipRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.StationRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.repo.TrainRunRepository;

@Component
public class TrackService implements ITrackService {

	@Autowired
	TrainRunRepository trainRunRepository;

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
				for (RailItemSequence railItemSequence : railItemSequenceRepository.findByTrack(track)) {
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
}