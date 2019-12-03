package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import de.gravitex.trainmaster.entity.enumeration.TrainRunState;
import de.gravitex.trainmaster.exception.TrainRunException;
import lombok.Data;

@Entity
@Data
public class TrainRun extends BaseEntity implements CheckedEntity {

	@OneToMany
	private List<TrainRunSection> trainRunSections = new ArrayList<>();

	public int trainRunSectionIndex = 0;

	public TrainRunState trainRunState;

	public static TrainRun fromStationNames(StationInfo... stationInfos) {
		
		TrainRun result = new TrainRun();
		StationInfo stationInfoActual = null;
		StationInfo stationInfoFollowing = null;
		TrainRunSection trainRunSection = null;
		for (int index = 0; index < stationInfos.length - 1; index++) {
			stationInfoActual = stationInfos[index];
			stationInfoFollowing = stationInfos[index + 1];
			trainRunSection = new TrainRunSection();
			trainRunSection.setStationFrom(stationInfoActual);
			trainRunSection.setStationTo(stationInfoFollowing);
			result.getTrainRunSections().add(trainRunSection);
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

	@PrePersist
	@Override
	public void checkData() {
		
		/*
		if ((trainRunSections == null) || (trainRunSections.size() < 1)) {
			throw new TrainRunException("train run must have at least one section!!");			
		}
		*/
	}
}