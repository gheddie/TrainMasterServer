package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.TrainRunSectionNode;

@Repository
public interface StationInfoRepository extends JpaRepository<TrainRunSectionNode, Long> {

}