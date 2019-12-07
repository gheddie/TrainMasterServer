package de.gravitex.trainmaster.entity.trainrun;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import de.gravitex.trainmaster.entity.BaseEntity;
import de.gravitex.trainmaster.exception.TrainRunException;
import lombok.Data;

@Entity
@Data
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "train_run_id", "sectionIndex" }) })
public abstract class TrainRunSection extends BaseEntity {

	@NotNull
	@ManyToOne
	private TrainRun trainRun;

	@NotNull
	private Integer sectionIndex = 0;

	@NotNull
	@OneToOne
	private TrainRunSectionDepartureNode nodeFrom;

	@NotNull
	@OneToOne
	private TrainRunSectionArrivalNode nodeTo;

	@PrePersist
	public void checkNodeTracks() {

		if ((nodeFrom.getExitTrack() == null) && (exitTrackMandatory())) {
			throw new TrainRunException("exit track mandatory for entity of class " + getClass().getSimpleName()
					+ " [node:" + nodeFrom.getStationFrom().getStationName() + "]!!");
		}

		if ((nodeTo.getEntryTrack() == null) && (entryTrackMandatory())) {
			throw new TrainRunException("entry track mandatory for entity of class " + getClass().getSimpleName()
					+ " [node:" + nodeTo.getStationTo().getStationName() + "]!!");
		}
	}

	protected abstract boolean exitTrackMandatory();

	protected abstract boolean entryTrackMandatory();
}