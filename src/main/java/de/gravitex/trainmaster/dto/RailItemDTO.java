package de.gravitex.trainmaster.dto;

import de.gravitex.trainmaster.entity.RailItem;
import lombok.Data;

@Data
public class RailItemDTO extends SingleEntityServerDTO<RailItem> {

	private String identifier;

	@Override
	public void fillValues(RailItem entity) {
		setIdentifier(entity.getIdentifier());
	}
}