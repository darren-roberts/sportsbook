package uk.co.raptorsoftware.examples.sportsbook.persistence;

import org.springframework.data.repository.CrudRepository;

import uk.co.raptorsoftware.examples.sportsbook.entities.ScoreEntity;

public interface ScoreRepository extends CrudRepository<ScoreEntity, Long> {

}
