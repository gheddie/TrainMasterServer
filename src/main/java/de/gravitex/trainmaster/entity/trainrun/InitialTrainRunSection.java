package de.gravitex.trainmaster.entity.trainrun;

import javax.persistence.Entity;

@Entity
public class InitialTrainRunSection extends TrainRunSection {

	@Override
	protected boolean exitTrackMandatory() {
		return true;
	}

	@Override
	protected boolean entryTrackMandatory() {
		return false;
	}
}