package de.gravitex.trainmaster.manager;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.helper.StringHelper;

public class WaggonManager {

	public static RailItemSequence addWaggonToSequence(RailItemSequence railItemSequence, RailItem... railItems) {
		if (railItemSequence == null) {
			railItemSequence = new RailItemSequence(0);
		}
		for (RailItem railItem : railItems) {
			RailItemSequenceMembership railItemSequenceMembership = new RailItemSequenceMembership();
			railItemSequenceMembership.setRailItem(railItem);
			railItemSequence.getRailItemSequenceMemberships().add(railItemSequenceMembership);
		}
		return railItemSequence;
	}

	public static String getWaggonNumbersAsString(RailItemSequence railItemSequence) {
		List<String> waggonNumbers = new ArrayList<>();
		for (RailItemSequenceMembership membership : railItemSequence.getRailItemSequenceMemberships()) {
			waggonNumbers.add(membership.getRailItem().getIdentifier());
		}
		return StringHelper.stringListAsOrderedAndSeparated(waggonNumbers);
	}

	public static RailItemSequence reverseWaggonSequence(RailItemSequence railItemSequence) {
		RailItemSequence reversedSequence = new RailItemSequence(0);
		int removeIndex = railItemSequence.getRailItemSequenceMemberships().size() - 1;
		for (int counter = 0; counter < railItemSequence.getRailItemSequenceMemberships().size(); counter++) {
			RailItemSequenceMembership membership = railItemSequence.getRailItemSequenceMemberships().get(removeIndex);
			membership.setOrdinalPosition(counter);
			addWaggonToSequence(reversedSequence, membership.getRailItem());
			removeIndex--;
		}
		return reversedSequence;
	}
}