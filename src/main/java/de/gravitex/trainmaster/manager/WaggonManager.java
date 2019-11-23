package de.gravitex.trainmaster.manager;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.RailtItemSequence;
import de.gravitex.trainmaster.helper.StringHelper;

public class WaggonManager {

	public static RailtItemSequence addWaggonToSequence(RailtItemSequence railtItemSequence, RailItem... railItems) {
		if (railtItemSequence == null) {
			railtItemSequence = new RailtItemSequence();
		}
		for (RailItem railItem : railItems) {
			RailItemSequenceMembership railItemSequenceMembership = new RailItemSequenceMembership();
			railItemSequenceMembership.setRailItem(railItem);
			railtItemSequence.getRailItemSequenceMemberships().add(railItemSequenceMembership);
		}
		return railtItemSequence;
	}

	public static String getWaggonNumbersAsString(RailtItemSequence railtItemSequence) {
		List<String> waggonNumbers = new ArrayList<>();
		for (RailItemSequenceMembership membership : railtItemSequence.getRailItemSequenceMemberships()) {
			waggonNumbers.add(membership.getRailItem().getIdentifier());
		}
		return StringHelper.stringListAsOrderedAndSeparated(waggonNumbers);
	}

	public static RailtItemSequence reverseWaggonSequence(RailtItemSequence railtItemSequence) {
		RailtItemSequence reversedSequence = new RailtItemSequence();
		int removeIndex = railtItemSequence.getRailItemSequenceMemberships().size() - 1;
		for (int counter = 0; counter < railtItemSequence.getRailItemSequenceMemberships().size(); counter++) {
			RailItemSequenceMembership membership = railtItemSequence.getRailItemSequenceMemberships().get(removeIndex);
			membership.setOrdinalPosition(counter);
			addWaggonToSequence(reversedSequence, membership.getRailItem());
			removeIndex--;
		}
		return reversedSequence;
	}
}