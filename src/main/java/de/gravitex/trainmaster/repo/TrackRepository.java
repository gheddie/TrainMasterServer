package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

}