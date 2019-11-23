package de.gravitex.trainmaster.helper;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.RailItemSequence;
import lombok.Data;

@Data
public class RailItemSequenceBuilder {

	private RailItem[] railItems;

	public RailItemSequenceBuilder withRailItems(RailItem... aRailItems) {
		this.railItems = aRailItems;
		return this;
	}

	public RailItemSequence build() {
		RailItemSequence result = new RailItemSequence();
		RailItemSequenceMembership membership = null;
		for (RailItem railItem : railItems) {
			membership = new RailItemSequenceMembership();
			membership.setRailItem(railItem.asConcreteItem());
			result.getRailItemSequenceMemberships().add(membership);
		}
		return result;
	}
}