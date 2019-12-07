package de.gravitex.trainmaster.entity.trainrun;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import de.gravitex.trainmaster.entity.BaseEntity;
import lombok.Data;

@Entity
@Data
public class TrainRun extends BaseEntity {

	@OneToMany
	private List<TrainRunSection> trainRunSections = new ArrayList<>();

	public int actualTrainRunSectionIndex = 0;

	public TrainRunSection getTrainRunSectionByIndex() {
		return trainRunSections.get(actualTrainRunSectionIndex);
	}

	public boolean getFinalIndex() {
		return (actualTrainRunSectionIndex == trainRunSections.size() - 1);
	}

	public void increaseTrainRunSectionIndex() {
		this.actualTrainRunSectionIndex++;
	}
}