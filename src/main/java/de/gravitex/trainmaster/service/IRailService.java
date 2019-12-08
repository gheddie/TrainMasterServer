package de.gravitex.trainmaster.service;

import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;

public interface IRailService {

	String getTrackSequenceAsString(String trackNumber);

	StationsAndTracksAndWaggonsDTO getStationsAndTracksAndWaggonsDTO();
	
	/*
	void createTrainRunSection(TrainRun trainRun, Station stationFrom, Station stationTo, Track entryTrack,
			Track exitTrack, int sectionIndex, int totalStationCount);
			*/
}