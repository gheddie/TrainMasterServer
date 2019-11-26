package de.gravitex.trainmaster.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class StationDTO implements ServerDTO {
	
	private String name;

	private List<TrackDTO> trackDTOs;

	public void addTrack(TrackDTO trackDTO) {
		if (trackDTOs == null) {
			trackDTOs = new ArrayList<TrackDTO>();
		}
		trackDTOs.add(trackDTO);
	}
}