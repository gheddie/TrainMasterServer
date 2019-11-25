package de.gravitex.trainmaster.util;

import java.util.HashMap;
import java.util.List;

import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import lombok.Data;

@Data
public class SimpleTrackRenderer {

	private HashMap<Station, StationsAndTracksAndWaggons> stationsAndTracksAndWaggons = new HashMap<Station, SimpleTrackRenderer.StationsAndTracksAndWaggons>();
	
	private String description;

	public void render() {
		System.out.println("---------"+description+"------------------------------------");

		StationsAndTracksAndWaggons sataw = null;
		for (Station station : stationsAndTracksAndWaggons.keySet()) {
			System.out.println("[@@@ STATION :: " + station.getStationName() + " @@@]");
			sataw = stationsAndTracksAndWaggons.get(station);
			List<RailItemSequenceMembership> itemsByTrack = null;
			for (Track t : sataw.getRailItemsByTrack().keySet()) {
				itemsByTrack = sataw.getRailItemsByTrack().get(t);
				if (itemsByTrack != null) {
					String itemString = "";
					for (RailItemSequenceMembership membership : itemsByTrack) {
						itemString += "[" + membership.getRailItem().getIdentifier() + "::"
								+ membership.getRailItemSequence().getOrdinalPosition() + "/"
								+ membership.getOrdinalPosition() + "]";
					}
					System.out.println("[" + t.getName() + "] --> " + itemString);
				}
			}
		}

		System.out.println("---------------------------------------------");
	}

	public void putTrackWaggons(Track track, List<RailItemSequenceMembership> railItems) {
		if (stationsAndTracksAndWaggons.get(track.getStation()) == null) {
			stationsAndTracksAndWaggons.put(track.getStation(), new StationsAndTracksAndWaggons());
		}
		stationsAndTracksAndWaggons.get(track.getStation()).getRailItemsByTrack().put(track, railItems);
	}

	// ---

	@Data
	private class StationsAndTracksAndWaggons {

		private HashMap<Track, List<RailItemSequenceMembership>> railItemsByTrack = new HashMap<Track, List<RailItemSequenceMembership>>();
	}
}