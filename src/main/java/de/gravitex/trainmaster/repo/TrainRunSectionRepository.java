package de.gravitex.trainmaster.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.trainrun.TrainRun;
import de.gravitex.trainmaster.entity.trainrun.TrainRunSection;

@Repository
public interface TrainRunSectionRepository extends JpaRepository<TrainRunSection, Long> {

	List<TrainRunSection> findByTrainRun(TrainRun trainRun);
}