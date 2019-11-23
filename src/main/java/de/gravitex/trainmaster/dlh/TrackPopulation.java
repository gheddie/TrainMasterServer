package de.gravitex.trainmaster.dlh;

import java.util.List;

import de.gravitex.trainmaster.entity.RailtItemSequence;
import de.gravitex.trainmaster.entity.Track;
import lombok.Data;

@Data
public class TrackPopulation {

	private Track track;
	
	private RailtItemSequence locomotiveSequence;
	
	private List<RailtItemSequence> waggonSequences;

	public TrackPopulation(Track track, RailtItemSequence locomotiveSequence, List<RailtItemSequence> waggonSequences) {
		super();
		this.track = track;
		this.locomotiveSequence = locomotiveSequence;
		this.waggonSequences = waggonSequences;
	}
}