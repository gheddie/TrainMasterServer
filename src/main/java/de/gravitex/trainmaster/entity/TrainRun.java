package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import de.gravitex.trainmaster.entity.enumeration.TrainState;
import de.gravitex.trainmaster.exception.TrainRunException;
import lombok.Data;

@Entity
@Data
public class TrainRun extends BaseEntity implements CheckedEntity {

	@OneToMany
	private List<TrainRunSection> trainRunSections = new ArrayList<>();

	public int actualTrainRunSectionIndex = 0;

	public static TrainRun fromStationNames(TrainRunSectionNode... stationInfos) {
		
		TrainRun result = new TrainRun();
		TrainRunSectionNode stationInfoActual = null;
		TrainRunSectionNode stationInfoFollowing = null;
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
		return trainRunSections.get(actualTrainRunSectionIndex);
	}

	public boolean getFinalIndex() {
		return (actualTrainRunSectionIndex == trainRunSections.size() - 1);
	}

	public void increaseTrainRunSectionIndex() {
		this.actualTrainRunSectionIndex++;
	}

	@PrePersist
	@Override
	public void checkData() {
		
		if ((trainRunSections != null) && (trainRunSections.size() > 0)) {
			int werner = 5;			
		}
		
		/*
		if ((trainRunSections == null) || (trainRunSections.size() < 1)) {
			throw new TrainRunException("train run must have at least one section!!");			
		}
		*/
	}
}