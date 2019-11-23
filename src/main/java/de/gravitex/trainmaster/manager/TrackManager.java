package de.gravitex.trainmaster.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.gravitex.trainmaster.dlh.TrackPopulation;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.RailtItemSequence;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.helper.StringHelper;

public class TrackManager {

	public static TrackPopulation populateTrack(Track track, RailtItemSequence locomotiveSequence, RailtItemSequence... waggonSequences) {
		// locos
		if (locomotiveSequence != null) {
			locomotiveSequence.setRailtItemSequenceHolder(track);
			track.getRailItemSequences().add(locomotiveSequence);
			locomotiveSequence.setRailtItemSequenceHolder(track);
		}
		// waggons
		for (RailtItemSequence railtItemSequence : waggonSequences) {
			railtItemSequence.setRailtItemSequenceHolder(track);
			track.getRailItemSequences().add(railtItemSequence);
			railtItemSequence.setRailtItemSequenceHolder(track);
		}
		return new TrackPopulation(track, locomotiveSequence, Arrays.asList(waggonSequences));
	}

	public static String getRailItemIdetifiersAsString(Track track) {
		List<String> waggonNumbers = new ArrayList<>();
		for (RailtItemSequence waggonSequence : track.getRailItemSequences()) {
			for (RailItemSequenceMembership membership : waggonSequence.getRailItemSequenceMemberships()) {
				waggonNumbers.add(membership.getRailItem().getIdentifier());
			}
		}
		return StringHelper.stringListAsOrderedAndSeparated(waggonNumbers);
	}
}