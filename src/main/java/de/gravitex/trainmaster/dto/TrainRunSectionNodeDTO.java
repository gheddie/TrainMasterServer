package de.gravitex.trainmaster.dto;

import de.gravitex.trainmaster.entity.trainrun.TrainRunSectionNode;
import lombok.Data;

@Data
public class TrainRunSectionNodeDTO extends SingleEntityServerDTO<TrainRunSectionNode> {
	
	private String stationFrom;
	
	private String stationTo;
	
	private String entryTrack;
	
	private String exitTrack;

	@Override
	public void fillValues(TrainRunSectionNode entity) {
		// TODO Auto-generated method stub
	}
}