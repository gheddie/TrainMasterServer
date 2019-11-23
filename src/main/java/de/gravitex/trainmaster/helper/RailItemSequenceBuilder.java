package de.gravitex.trainmaster.helper;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.RailtItemSequence;
import lombok.Data;

@Data
public class RailItemSequenceBuilder {

	private RailItem[] railItems;

	public RailItemSequenceBuilder withRailItems(RailItem... aRailItems) {
		this.railItems = aRailItems;
		return this;
	}

	public RailtItemSequence build() {
		RailtItemSequence result = new RailtItemSequence();
		RailItemSequenceMembership membership = null;
		for (RailItem railItem : railItems) {
			membership = new RailItemSequenceMembership(railItem.asConcreteItem());
			result.getRailItemSequenceMemberships().add(membership);
		}
		return result;
	}
}