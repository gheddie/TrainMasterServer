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

	public void addRailItem(StationDTO stationDTO, TrackDTO trackDTO, RailItemDTO railItemDTO) {
		for (StationDTO s : stationDTOs) {
			if (s.equals(stationDTO)) {
				for (TrackDTO t : s.getTrackDTOs()) {
					if (t.equals(trackDTO)) {
						t.addRailItem(railItemDTO);
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
		List<String> identifiers = new ArrayList<String>();
		for (RailItemDTO railItemDTO : trackDTO.getRailItemDTOs()) {
			identifiers.add(railItemDTO.getIdentifier());
		}
		return RailItemSequcenFormatter.format(identifiers);
	}

	private TrackDTO findTrack(StationDTO station, String trackNumber) {
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