package de.gravitex.trainmaster.request;

import java.util.List;

import de.gravitex.trainmaster.dto.TrainRunSectionNodeDTO;
import lombok.Data;

@Data
public class TrainRunDescriptor {

	private List<TrainRunSectionNodeDTO> stationInfoDTOs;
	
	private String trainNumber;
	
	private String sequenceIdentifier;
}