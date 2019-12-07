package de.gravitex.trainmaster.entity.trainrun;

import javax.persistence.Entity;

@Entity
public class FinalTrainRunSection extends TrainRunSection {

	@Override
	protected boolean exitTrackMandatory() {
		return false;
	}

	@Override
	protected boolean entryTrackMandatory() {
		return true;
	}
}