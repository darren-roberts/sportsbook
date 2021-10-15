package uk.co.raptorsoftware.examples.sportsbook.persistence;

import org.springframework.data.repository.CrudRepository;

import uk.co.raptorsoftware.examples.sportsbook.entities.TeamEntity;

public interface TeamRepository extends CrudRepository<TeamEntity, Long> {

}
