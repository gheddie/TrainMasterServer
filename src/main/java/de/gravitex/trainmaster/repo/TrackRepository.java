package de.gravitex.trainmaster.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.Station;
import de.gravitex.trainmaster.entity.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

	List<Track> findByStation(Station station);

	Track findByTrackNumber(String trackNumber);
}