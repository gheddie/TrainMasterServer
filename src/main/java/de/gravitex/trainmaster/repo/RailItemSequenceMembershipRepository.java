package de.gravitex.trainmaster.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.RailItemSequence;
import de.gravitex.trainmaster.entity.RailItemSequenceMembership;

@Repository
public interface RailItemSequenceMembershipRepository extends JpaRepository<RailItemSequenceMembership, Long> {

	List<RailItemSequenceMembership> findByRailItemSequence(RailItemSequence railItemSequence);
}