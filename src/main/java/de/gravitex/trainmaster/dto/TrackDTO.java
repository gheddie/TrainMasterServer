package de.gravitex.trainmaster.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TrackDTO implements ServerDTO {

	private List<RailItemDTO> railItemDTOs;
	
	private String trackNumber;

	public void addRailItem(RailItemDTO railItemDTO) {
		if (railItemDTOs == null) {
			railItemDTOs = new ArrayList<RailItemDTO>();
		}
		railItemDTOs.add(railItemDTO);
	}
}