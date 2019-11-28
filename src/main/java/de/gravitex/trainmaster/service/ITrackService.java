package de.gravitex.trainmaster.service;

import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;

public interface ITrackService {

	String getTrackSequenceAsString(String trackNumber);

	StationsAndTracksAndWaggonsDTO getStationsAndTracksAndWaggonsDTO();
}