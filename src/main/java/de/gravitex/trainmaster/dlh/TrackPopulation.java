package de.gravitex.trainmaster.dlh;

import java.util.List;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Track;
import lombok.Data;

@Data
public class TrackPopulation {

	private Track track;
	
	private RailItemSequence locomotiveSequence;
	
	private List<RailItemSequence> waggonSequences;

	public TrackPopulation(Track track, RailItemSequence locomotiveSequence, List<RailItemSequence> waggonSequences) {
		super();
		this.track = track;
		this.locomotiveSequence = locomotiveSequence;
		this.waggonSequences = waggonSequences;
	}
}