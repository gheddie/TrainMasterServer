package de.gravitex.trainmaster.dto;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.trainmaster.entity.Station;
import lombok.Data;

@Data
public class StationDTO extends SingleEntityServerDTO<Station> {
	
	private String stationName;

	private List<TrackDTO> trackDTOs;

	public void addTrack(TrackDTO trackDTO) {
		if (trackDTOs == null) {
			trackDTOs = new ArrayList<TrackDTO>();
		}
		trackDTOs.add(trackDTO);
	}

	@Override
	public void fillValues(Station entity) {
		setStationName(entity.getStationName());
	}
}