package de.gravitex.trainmaster.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;
import de.gravitex.trainmaster.entity.Track;

@Repository
public interface RailItemRepository extends JpaRepository<RailItem, Long> {

	@Query(value = "SELECT rism FROM RailItemSequenceMembership rism INNER JOIN rism.railItemSequence seq INNER JOIN seq.railItemSequenceHolder WHERE seq.railItemSequenceHolder = :track ORDER BY seq.ordinalPosition ASC, rism.ordinalPosition ASC")
	List<RailItemSequenceMembership> findByTrack(Track track);
}