package de.gravitex.trainmaster.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.gravitex.trainmaster.entity.RailItem;

@Repository
public interface RailItemRepository extends JpaRepository<RailItem, Long> {

}