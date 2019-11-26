package de.gravitex.trainmaster.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.Track;
import de.gravitex.trainmaster.repo.RailItemSequenceMembershipRepository;
import de.gravitex.trainmaster.repo.RailItemSequenceRepository;
import de.gravitex.trainmaster.repo.TrackRepository;
import de.gravitex.trainmaster.repo.TrainRunRepository;

@Component
public class TrackService implements ITrackService {

	@Autowired
	TrainRunRepository trainRunRepository;

	@Autowired
	TrackRepository trackRepository;
	
	@Autowired
	RailItemSequenceMembershipRepository railItemSequenceMembershipRepository;
	
	@Autowired
	RailItemSequenceRepository railItemSequenceRepository;

	@Override
	public String getTrackSequenceAsString(String trackNumber) {
		Track track = trackRepository.findByTrackNumber(trackNumber);
		List<RailItemSequenceMembership> sequenceMemberships = null;
		String result = "";
		for (RailItemSequence railItemSequence : railItemSequenceRepository.findByRailItemSequenceHolder(track)) {
			sequenceMemberships = railItemSequenceMembershipRepository.findByRailItemSequence(railItemSequence);
			for (RailItemSequenceMembership sequenceMembership : sequenceMemberships) {
				result += sequenceMembership.getRailItem().getIdentifier() + "#";
			}
		}
		// return "moo123_" + trainRunRepository.findAll().size();
		return result;
	}
}