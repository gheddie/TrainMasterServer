package de.gravitex.trainmaster.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class StationAndTracksAndWaggonsDTO implements ServerDTO {

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
}