package de.gravitex.trainmaster.dto;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.trainmaster.entity.Track;
import lombok.Data;

@Data
public class TrackDTO  extends SingleEntityServerDTO<Track> {

	private List<RailItemDTO> railItemDTOs;
	
	private String trackNumber;

	public void addRailItem(RailItemDTO railItemDTO) {
		if (railItemDTOs == null) {
			railItemDTOs = new ArrayList<RailItemDTO>();
		}
		railItemDTOs.add(railItemDTO);
	}

	@Override
	public void fillValues(Track entity) {
		setTrackNumber(entity.getTrackNumber());
	}
}