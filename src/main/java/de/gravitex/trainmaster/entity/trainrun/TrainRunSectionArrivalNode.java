package de.gravitex.trainmaster.entity.trainrun;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;
import lombok.Data;

@Entity
@Data
public class TrainRunSectionArrivalNode extends TrainRunSectionNode {
	
	@NotNull
	@OneToOne
	private Station stationTo;

	@OneToOne
	private Track entryTrack;
}