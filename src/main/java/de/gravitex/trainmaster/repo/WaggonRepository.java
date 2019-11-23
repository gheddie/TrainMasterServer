package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.Waggon;

@Repository
public interface WaggonRepository extends JpaRepository<Waggon, Long> {

}