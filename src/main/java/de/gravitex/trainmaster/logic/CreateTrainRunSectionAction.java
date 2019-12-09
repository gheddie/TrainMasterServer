package de.gravitex.trainmaster.logic;

import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.entity.trainrun.FinalTrainRunSection;
import de.gravitex.trainmaster.entity.trainrun.InitialTrainRunSection;
import de.gravitex.trainmaster.entity.trainrun.IntermediateTrainRunSection;
import de.gravitex.trainmaster.entity.trainrun.TrainRun;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSection;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSectionArrivalNode;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSectionDepartureNode;
import de.gravitex.trainmaster.request.TrainRunDescriptor;
import lombok.Data;

@Data
public class CreateTrainRunSectionAction extends TrainRunAction {

	private Station stationFrom;
	
	private Station stationTo;
	
	private Track entryTrack;
	
	private Track exitTrack;
	
	private int index;
	
	private TrainRunDescriptor trainRunDescriptor;

	private TrainRun trainRun;

	private TrainRunSectionDepartureNode trainRunSectionNodeFrom;

	private TrainRunSectionArrivalNode trainRunSectionNodeTo;

	private TrainRunSection trainRunSection;

	@Override
	public void execute() {
		
		createTrainRunSection(trainRun,
				stationFrom,
				stationTo,
				entryTrack,
				exitTrack, index,
				trainRunDescriptor.getStationInfoDTOs().size());
	}
	
	private void createTrainRunSection(TrainRun trainRun, Station stationFrom, Station stationTo, Track entryTrack,
			Track exitTrack, int sectionIndex, int totalStationCount) {
		
		trainRunSectionNodeFrom = new TrainRunSectionDepartureNode();
		trainRunSectionNodeFrom.setStationFrom(stationFrom);
		trainRunSectionNodeFrom.setExitTrack(exitTrack);

		trainRunSectionNodeTo = new TrainRunSectionArrivalNode();
		trainRunSectionNodeTo.setStationTo(stationTo);
		trainRunSectionNodeTo.setEntryTrack(entryTrack);

		trainRunSection = null;

		if (sectionIndex == 0) {
			trainRunSection = new InitialTrainRunSection();
		} else if (sectionIndex == totalStationCount) {
			trainRunSection = new IntermediateTrainRunSection();
		} else {
			trainRunSection = new FinalTrainRunSection();
		}

		trainRunSection.setNodeFrom(trainRunSectionNodeFrom);
		trainRunSection.setNodeTo(trainRunSectionNodeTo);
		
		trainRunSection.setTrainRun(trainRun);
		trainRunSection.setSectionIndex(sectionIndex);
	}
}