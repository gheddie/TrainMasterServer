package de.gravitex.trainmaster.util;

import java.util.HashMap;
import java.util.List;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import lombok.Data;

@Data
public class SimpleTrackRenderer {

	private HashMap<Station, StationsAndTracksAndWaggons> stationsAndTracksAndWaggons = new HashMap<Station, SimpleTrackRenderer.StationsAndTracksAndWaggons>();

	public void render() {
		System.out.println("---------------------------------------------");

		StationsAndTracksAndWaggons sataw = null;
		for (Station station : stationsAndTracksAndWaggons.keySet()) {
			System.out.println("[@@@ STATION :: "+station.getStationName()+" @@@]");
			sataw = stationsAndTracksAndWaggons.get(station);
			List<RailItem> itemsByTrack = null;
			for (Track t : sataw.getRailItemsByTrack().keySet()) {
				itemsByTrack = sataw.getRailItemsByTrack().get(t);
				if (itemsByTrack != null) {
					String itemString = "";
					for (RailItem item : itemsByTrack) {
						itemString += "[" + item.getIdentifier() + "]";
					}
					System.out.println("[" + t.getName() + "] --> " + itemString);
				}
			}
		}

		System.out.println("---------------------------------------------");
	}

	public void putTrackWaggons(Track track, List<RailItem> railItems) {
		if (stationsAndTracksAndWaggons.get(track.getStation()) == null) {
			stationsAndTracksAndWaggons.put(track.getStation(), new StationsAndTracksAndWaggons());	
		}
		stationsAndTracksAndWaggons.get(track.getStation()).getRailItemsByTrack().put(track, railItems);
	}

	// ---

	@Data
	private class StationsAndTracksAndWaggons {

		private HashMap<Track, List<RailItem>> railItemsByTrack = new HashMap<Track, List<RailItem>>();
	}
}