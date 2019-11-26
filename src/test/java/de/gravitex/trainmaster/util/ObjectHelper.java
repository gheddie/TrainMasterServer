package de.gravitex.trainmaster.util;

import de.gravitex.trainmaster.dto.RailItemDTO;
import de.gravitex.trainmaster.dto.StationAndTracksAndWaggonsDTO;
import de.gravitex.trainmaster.dto.StationDTO;
import de.gravitex.trainmaster.dto.TrackDTO;

public class ObjectHelper {

	public static void renderStation(StationAndTracksAndWaggonsDTO stationAndTracksAndWaggons) {
		for (StationDTO s : stationAndTracksAndWaggons.getStationDTOs()) {
			System.out.println("------------------------------------------------------------------------");
			System.out.println("[STATION] " + s.getName());
			System.out.println("------------------------------------------------------------------------");
			for (TrackDTO t : s.getTrackDTOs()) {
				String itemString = "";
				for (RailItemDTO r : t.getRailItemDTOs()) {
					itemString += "[" + r.getIdentifier() + "]";
				}
				System.out.println(
						"[TRACK::" + t.getTrackNumber() + "] " + " " + itemString);
			}
			System.out.println("------------------------------------------------------------------------");
		}
	}
}