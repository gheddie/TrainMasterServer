package de.gravitex.trainmaster.entity.trainrun;

import javax.persistence.Entity;

@Entity
public class IntermediateTrainRunSection extends TrainRunSection {

	@Override
	protected boolean exitTrackMandatory() {
		return true;
	}

	@Override
	protected boolean entryTrackMandatory() {
		return true;
	}
}