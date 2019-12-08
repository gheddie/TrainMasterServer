package de.gravitex.trainmaster.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.gravitex.trainmaster.config.ServerMappings;
import de.gravitex.trainmaster.dto.RailItemDTO;
import de.gravitex.trainmaster.dto.RailItemSequenceDTO;
import de.gravitex.trainmaster.dto.StationsAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.dto.StationDTO;
import de.gravitex.trainmaster.dto.TrackDTO;

public class TrainRunTestUtil {

	public static void renderStation(StationsAndTracksAndWaggonsDTO stationAndTracksAndWaggons) {
		for (StationDTO s : stationAndTracksAndWaggons.getStationDTOs()) {
			System.out.println("------------------------------------------------------------------------");
			System.out.println("[STATION] " + s.getStationName());
			System.out.println("------------------------------------------------------------------------");
			for (TrackDTO t : s.getTrackDTOs()) {
				String itemString = "";
				for (RailItemSequenceDTO ris : t.getRailItemSequenceDTOs()) {
					for (RailItemDTO ri : ris.getRailItemDTOs()) {
						itemString += "[" + ri.getIdentifier() + "]";						
					}
				}
				System.out.println(
						"[TRACK::" + t.getTrackNumber() + "] " + " " + itemString);
			}
			System.out.println("------------------------------------------------------------------------");
		}
	}
	
	public static void assertTrackSequence(String station,
			String track, String expectedTrackSequence, MockMvc mockMvc) throws Exception {
		
		StationsAndTracksAndWaggonsDTO stationAndTracksAndWaggons = getStationDataFromServer(mockMvc);
		String actualTrackWaggonsAsString = stationAndTracksAndWaggons.getTrackWaggonsAsString(station, track);
		Assert.isTrue(actualTrackWaggonsAsString.equals(expectedTrackSequence));
	}
	
	public static StationsAndTracksAndWaggonsDTO getStationDataFromServer(MockMvc mockMvc) throws Exception {

		ResultActions resultActions = mockMvc.perform(get(ServerMappings.TrainRun.STATION_DATA));
		String json = resultActions.andReturn().getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		StationsAndTracksAndWaggonsDTO sataw = mapper.readValue(json, StationsAndTracksAndWaggonsDTO.class);
		return sataw;
	}
}