package de.gravitex.trainmaster.request;

import java.util.List;

import de.gravitex.trainmaster.dto.StationInfoDTO;
import lombok.Data;

@Data
public class TrainRunDescriptor {

	private List<StationInfoDTO> stationInfoDTOs;
}