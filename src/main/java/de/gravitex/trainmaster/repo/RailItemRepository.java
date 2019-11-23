package de.gravitex.trainmaster.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.RailItem;
import de.gravitex.trainmaster.entity.Track;

@Repository
public interface RailItemRepository extends JpaRepository<RailItem, Long> {

	@Query(value = "SELECT rism.railItem FROM RailItemSequenceMembership rism INNER JOIN rism.railtItemSequence seq INNER JOIN seq.railtItemSequenceHolder WHERE seq.railtItemSequenceHolder = :track")
	List<RailItem> findByTrack(Track track);
}