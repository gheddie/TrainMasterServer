package de.gravitex.trainmaster.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.Track;

@Repository
public interface RailItemSequenceRepository extends JpaRepository<RailItemSequence, Long> {

	List<RailItemSequence> findByTrack(Track track);
}