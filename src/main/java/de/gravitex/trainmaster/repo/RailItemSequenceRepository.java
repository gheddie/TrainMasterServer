package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.RailtItemSequence;

@Repository
public interface RailItemSequenceRepository extends JpaRepository<RailtItemSequence, Long> {

}