package de.gravitex.trainmaster.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.gravitex.trainmaster.common.RailItemSequcenFormatter;
import lombok.Data;

@Data
public class StationsAndTracksAndWaggonsDTO implements ServerDTO {

	private List<StationDTO> stationDTOs;

	public void addStation(StationDTO stationDTO) {
		if (stationDTOs == null) {
			stationDTOs = new ArrayList<StationDTO>();
		}
		stationDTOs.add(stationDTO);
	}

	public void addTrack(StationDTO stationDTO, TrackDTO trackDTO) {
		for (StationDTO s : stationDTOs) {
			if (s.equals(stationDTO)) {
				s.addTrack(trackDTO);
			}
		}
	}

	public void addRailItemSequence(StationDTO stationDTO, TrackDTO trackDTO, RailItemSequenceDTO railItemSequenceDTO) {
		for (StationDTO s : stationDTOs) {
			if (s.equals(stationDTO)) {
				for (TrackDTO t : s.getTrackDTOs()) {
					if (t.equals(trackDTO)) {
						t.addRailItemSequence(railItemSequenceDTO);
					}
				}
			}
		}
	}

	public void addRailItem(StationDTO stationDTO, TrackDTO trackDTO, RailItemSequenceDTO railItemSequenceDTO,
			RailItemDTO railItemDTO) {
		for (StationDTO s : stationDTOs) {
			if (s.equals(stationDTO)) {
				for (TrackDTO t : s.getTrackDTOs()) {
					if (t.equals(trackDTO)) {
						for (RailItemSequenceDTO ris : t.getRailItemSequenceDTOs()) {
							if (ris.equals(railItemSequenceDTO)) {
								ris.addRailItem(railItemDTO);
							}
						}
					}
				}
			}
		}
	}

	@JsonIgnore
	public String getTrackWaggonsAsString(String stationName, String trackNumber) {

		StationDTO station = findStation(stationName);
		TrackDTO trackDTO = findTrack(station, trackNumber);
		if (trackDTO == null) {
			return "";
		}
		List<String> sequenceStrings = new ArrayList<String>();
		if (trackDTO.getRailItemSequenceDTOs() != null) {
			for (RailItemSequenceDTO railItemSequenceDTO : trackDTO.getRailItemSequenceDTOs()) {
				sequenceStrings.add(formatSequence(railItemSequenceDTO));
			}
		}
		return RailItemSequcenFormatter.format(trackNumber, sequenceStrings);
	}

	@JsonIgnore
	private String formatSequence(RailItemSequenceDTO railItemSequenceDTO) {
		String result = railItemSequenceDTO.getSequenceIdentifier() + "{";
		for (RailItemDTO railItemDTO : railItemSequenceDTO.getRailItemDTOs()) {
			result += "["+railItemDTO.getIdentifier()+"]";					
		}
		result += "}";
		return result;
	}

	private TrackDTO findTrack(StationDTO station, String trackNumber) {
		if (station == null) {
			return null;
		}
		if (station.getTrackDTOs() == null) {
			return null;
		}
		for (TrackDTO t : station.getTrackDTOs()) {
			if (t.getTrackNumber().equals(trackNumber)) {
				return t;
			}
		}
		return null;
	}

	@JsonIgnore
	private StationDTO findStation(String stationName) {
		for (StationDTO s : stationDTOs) {
			if (s.getStationName().equals(stationName)) {
				return s;
			}
		}
		return null;
	}
}