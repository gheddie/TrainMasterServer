package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.Train;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

	Train findByTrainNumber(String trainNumber);
}