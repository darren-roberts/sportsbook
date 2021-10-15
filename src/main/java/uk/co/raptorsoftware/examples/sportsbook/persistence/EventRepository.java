package uk.co.raptorsoftware.examples.sportsbook.persistence;

import org.springframework.data.repository.CrudRepository;

import uk.co.raptorsoftware.examples.sportsbook.entities.EventEntity;

public interface EventRepository extends CrudRepository<EventEntity, Long> {

}
