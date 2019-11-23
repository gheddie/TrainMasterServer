package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.TrainRun;

@Repository
public interface TrainRunRepository extends JpaRepository<TrainRun, Long> {

}