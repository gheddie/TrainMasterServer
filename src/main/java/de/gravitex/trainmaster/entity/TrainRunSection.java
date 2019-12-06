package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "train_run_id", "sectionIndex" }) })
public class TrainRunSection extends BaseEntity {

	@NotNull
	@ManyToOne
	private TrainRun trainRun;

	@NotNull
	private Integer sectionIndex = 0;

	@NotNull
	@OneToOne
	private TrainRunSectionNode stationFrom;

	@NotNull
	@OneToOne
	private TrainRunSectionNode stationTo;
}