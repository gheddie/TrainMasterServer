package de.gravitex.trainmaster.dto;

import javax.persistence.OneToOne;

import de.gravitex.trainmaster.entity.StationInfo;
import lombok.Data;

@Data
public class StationInfoDTO extends SingleEntityServerDTO<StationInfo> {
	
	private String station;
	
	@OneToOne
	private String entryTrack;
	
	@OneToOne
	private String exitTrack;

	@Override
	public void fillValues(StationInfo entity) {
		// TODO Auto-generated method stub
	}
}