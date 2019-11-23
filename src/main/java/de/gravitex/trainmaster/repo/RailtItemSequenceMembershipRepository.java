package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.RailItemSequenceMembership;

@Repository
public interface RailtItemSequenceMembershipRepository extends JpaRepository<RailItemSequenceMembership, Long> {

}