package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

	Station findByStationName(String stationName);
}