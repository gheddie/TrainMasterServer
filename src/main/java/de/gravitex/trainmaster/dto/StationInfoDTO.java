package de.gravitex.trainmaster.dto;

import javax.persistence.OneToOne;

import de.gravitex.trainmaster.entity.TrainRunSectionNode;
import lombok.Data;

@Data
public class StationInfoDTO extends SingleEntityServerDTO<TrainRunSectionNode> {
	
	private String station;
	
	@OneToOne
	private String entryTrack;
	
	@OneToOne
	private String exitTrack;

	@Override
	public void fillValues(TrainRunSectionNode entity) {
		// TODO Auto-generated method stub
	}
}