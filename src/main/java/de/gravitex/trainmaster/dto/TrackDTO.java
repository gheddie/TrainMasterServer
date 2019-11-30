package de.gravitex.trainmaster.dto;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.trainmaster.entity.Track;
import lombok.Data;

@Data
public class TrackDTO  extends SingleEntityServerDTO<Track> {

	private List<RailItemSequenceDTO> railItemSequenceDTOs;
	
	private String trackNumber;
	
	public void addRailItemSequence(RailItemSequenceDTO railItemSequenceDTO) {
		if (railItemSequenceDTOs == null) {
			railItemSequenceDTOs = new ArrayList<RailItemSequenceDTO>();
		}
		railItemSequenceDTOs.add(railItemSequenceDTO);
	}

	@Override
	public void fillValues(Track entity) {
		setTrackNumber(entity.getTrackNumber());
	}
}