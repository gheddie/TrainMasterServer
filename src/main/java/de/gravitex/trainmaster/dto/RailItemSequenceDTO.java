package de.gravitex.trainmaster.dto;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.trainmaster.entity.RailItemSequence;
import lombok.Data;

@Data
public class RailItemSequenceDTO extends SingleEntityServerDTO<RailItemSequence> {
	
	private List<RailItemDTO> railItemDTOs;
	
	private String sequenceIdentifier;
	
	public void addRailItem(RailItemDTO railItemDTO) {
		if (railItemDTOs == null) {
			railItemDTOs = new ArrayList<RailItemDTO>();
		}
		railItemDTOs.add(railItemDTO);
	}

	@Override
	public void fillValues(RailItemSequence entity) {
		setSequenceIdentifier(entity.getSequenceIdentifier());
	}
}