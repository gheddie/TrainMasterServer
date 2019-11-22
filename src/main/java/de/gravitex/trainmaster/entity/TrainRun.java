package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import de.gravitex.trainmaster.entity.enumeration.TrainRunState;
import lombok.Data;

@Entity
@Data
public class TrainRun extends BaseEntity {

	@OneToMany
	private List<TrainRunSection> trainRunSections = new ArrayList<>();

	public int trainRunSectionIndex = 0;

	public TrainRunState trainRunState;

	public static TrainRun fromStationNames(StationInfo... stationInfos) {
		TrainRun result = new TrainRun();
		for (int index = 0; index < stationInfos.length - 1; index++) {
			result.getTrainRunSections().add(new TrainRunSection(stationInfos[index], stationInfos[index + 1]));
		}
		return result;
	}

	public TrainRunSection getTrainRunSectionByIndex() {
		return trainRunSections.get(trainRunSectionIndex);
	}

	public boolean getFinalIndex() {
		return (trainRunSectionIndex == trainRunSections.size() - 1);
	}

	public void increaseTrainRunSectionIndex() {
		this.trainRunSectionIndex++;
	}
}