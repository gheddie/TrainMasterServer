package de.gravitex.trainmaster.logic;

import java.util.List;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.Train;
import de.gravitex.trainmaster.entity.trainrun.TrainRun;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSection;
import lombok.Data;

@Data
public class TrainRunArrivalAction extends TrainRunAction {

	private Train train;
	
	private List<TrainRunSection> sections;

	@Override
	public void execute() {
		
		TrainRun trainRun = train.getTrainRun();

		int actualTrainRunSectionIndex = trainRun.getActualTrainRunSectionIndex();
		TrainRunSection trainRunSection = sections.get(actualTrainRunSectionIndex);

		// increase train run index
		actualTrainRunSectionIndex++;
		trainRun.setActualTrainRunSectionIndex(actualTrainRunSectionIndex);

		// add waggons to entry track
		Track entryTrack = trainRunSection.getNodeTo().getEntryTrack();
		RailItemSequence sequence = train.getWaggonSequence();

		sequence.setTrack(entryTrack);
	}
}