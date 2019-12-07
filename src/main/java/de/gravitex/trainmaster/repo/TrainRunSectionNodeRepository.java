package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.trainrun.TrainRunSectionNode;

@Repository
public interface TrainRunSectionNodeRepository extends JpaRepository<TrainRunSectionNode, Long> {

}