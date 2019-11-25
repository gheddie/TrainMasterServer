package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.TrainRunSection;

@Repository
public interface TrainRunSectionRepository extends JpaRepository<TrainRunSection, Long> {

}