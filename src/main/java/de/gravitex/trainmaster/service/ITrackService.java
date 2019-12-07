package de.gravitex.trainmaster.service;

import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.trainrun.TrainRun;

public interface ITrackService {

	String getTrackSequenceAsString(String trackNumber);

	StationsAndTracksAndWaggonsDTO getStationsAndTracksAndWaggonsDTO();
	
	void createTrainRunSection(TrainRun trainRun, Station stationFrom, Station stationTo, Track entryTrack,
			Track exitTrack, int sectionIndex, int totalStationCount);
}