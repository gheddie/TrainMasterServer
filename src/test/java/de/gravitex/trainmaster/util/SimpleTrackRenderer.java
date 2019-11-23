package de.gravitex.trainmaster.util;

import java.util.HashMap;
import java.util.List;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import lombok.Data;

@Data
public class SimpleTrackRenderer {

	private List<Track> tracks;
	
	private HashMap<Station, Moo> moos;

	private HashMap<Track, List<RailItem>> railItemsByTrack = new HashMap<Track, List<RailItem>>();

	public SimpleTrackRenderer(List<Track> aTracks) {
		this.tracks = aTracks;
	}

	public void render() {
		System.out.println("---------------------------------------------");
		List<RailItem> itemsByTrack = null;
		for (Track t : tracks) {
			itemsByTrack = railItemsByTrack.get(t);
			if (itemsByTrack != null) {
				String itemString = "";
				for (RailItem item : itemsByTrack) {
					itemString += "[" + item.getIdentifier() + "]";
				}
				System.out.println("[" + t.getName() + "] --> " + itemString);
			}
		}
		System.out.println("---------------------------------------------");
	}

	public void putTrackWaggons(Track track, List<RailItem> railItems) {
		railItemsByTrack.put(track, railItems);
	}
	
	// ---
	
	private class Moo {
		
	}
}