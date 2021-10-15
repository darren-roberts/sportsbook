package uk.co.raptorsoftware.examples.sportsbook.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.raptorsoftware.examples.sportsbook.domain.Event;
import uk.co.raptorsoftware.examples.sportsbook.entities.EventEntity;
import uk.co.raptorsoftware.examples.sportsbook.entities.ScoreEntity;
import uk.co.raptorsoftware.examples.sportsbook.entities.TeamEntity;
import uk.co.raptorsoftware.examples.sportsbook.persistence.EventRepository;
import uk.co.raptorsoftware.examples.sportsbook.persistence.TeamRepository;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

	@Mock
	private EventRepository eventRepository;

	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private EventService eventService = new EventServiceImplementation();

	@Test
	void testSaveEvent() {

		String eventName = "Liverpool v Luton";
		LocalDateTime eventDateTime = LocalDateTime.parse("2019-01-25T20:00:00");
		Long teamAId = Long.valueOf(1);
		String teamAName = "Liverpool";
		Long teamBId = Long.valueOf(2);
		String teamBName = "Luton";
		TeamEntity teamEntityA = TeamEntity.builder().id(Long.valueOf(teamAId)).name(teamAName).build();
		TeamEntity teamEntityB = TeamEntity.builder().id(Long.valueOf(teamBId)).name(teamBName).build();
		Optional<TeamEntity> teamAOptional = Optional.of(teamEntityA);
		Optional<TeamEntity> teamBOptional = Optional.of(teamEntityB);
		List<ScoreEntity> scores = new ArrayList<ScoreEntity>();
		EventEntity savedEventEntity = EventEntity.builder().id(Long.valueOf(1)).name(eventName).scores(scores)
				.eventDateTime(eventDateTime).scores(scores).teamA(teamEntityA).teamB(teamEntityB).build();

		Event newEvent = Event.builder().name(eventName).eventDateTime(eventDateTime).teamAId(teamAId).teamBId(teamBId)
				.build();

		when(teamRepository.findById(teamAId)).thenReturn(teamAOptional);
		when(teamRepository.findById(teamBId)).thenReturn(teamBOptional);

		when(eventRepository.save(Mockito.any(EventEntity.class))).thenReturn(savedEventEntity);

		Event returnEvent = eventService.createEvent(newEvent);

		assertNotNull(returnEvent);
		assertNotNull(returnEvent.getId());
		assertEquals(eventName, returnEvent.getName());
		assertEquals(eventDateTime, returnEvent.getEventDateTime());

	}

	@Test
	void testEventTeamWithMissingTeamAThrowsValidationException() {
		Assertions.assertThrows(EventValidationException.class, () -> {

			String name = "Liverpool v Luton";
			LocalDateTime eventDateTime = LocalDateTime.parse("2019-01-25T20:00:00");
			Long teamBId = Long.valueOf(2);
			Event event = Event.builder().name(name).eventDateTime(eventDateTime).teamBId(teamBId).build();

			eventService.createEvent(event);

		});
	}

	@Test
	void testEventTeamWithMissingTeamBThrowsValidationException() {
		Assertions.assertThrows(EventValidationException.class, () -> {

			String name = "Liverpool v Luton";
			LocalDateTime eventDateTime = LocalDateTime.parse("2019-01-25T20:00:00");
			Long teamAId = (Long.valueOf(1));
			Event event = Event.builder().name(name).eventDateTime(eventDateTime).teamAId(teamAId).build();

			eventService.createEvent(event);

		});
	}

	@Test
	void testCreateEventWithMissingNameThrowsValidationException() {
		Assertions.assertThrows(EventValidationException.class, () -> {

			LocalDateTime eventDateTime = LocalDateTime.parse("2019-01-25T20:00:00");
			Long teamAId = Long.valueOf(1);
			Long teamBId = Long.valueOf(2);
			Event event = Event.builder().eventDateTime(eventDateTime).teamAId(teamAId).teamBId(teamBId).build();

			eventService.createEvent(event);

		});
	}

	@Test
	void testCreateEventWithemptyNameThrowsValidationException() {
		Assertions.assertThrows(EventValidationException.class, () -> {

			String name = "";
			LocalDateTime eventDateTime = LocalDateTime.parse("2019-01-25T20:00:00");
			Long teamAId = Long.valueOf(1);
			Long teamBId = Long.valueOf(2);
			Event event = Event.builder().name(name).eventDateTime(eventDateTime).teamAId(teamAId).teamBId(teamBId)
					.build();

			eventService.createEvent(event);

		});
	}

	@Test
	void testCreateEventWithMissingEventDateTimeThrowsValidationException() {
		Assertions.assertThrows(EventValidationException.class, () -> {

			String name = "Liverpool v Luton";
			Long teamAId = Long.valueOf(1);
			Long teamBId = Long.valueOf(2);
			Event event = Event.builder().name(name).teamAId(teamAId).teamBId(teamBId).build();

			eventService.createEvent(event);

		});
	}

	@Test
	void testCreateEventWithInvalidTeamIdTimeThrowsTeamNotFoundException() {

		Long incorrectTeamId = Long.valueOf(1);
		when(teamRepository.findById(incorrectTeamId)).thenReturn(Optional.empty());

		Assertions.assertThrows(TeamNotFoundException.class, () -> {

			String name = "Liverpool v Luton";
			LocalDateTime eventDateTime = LocalDateTime.parse("2019-01-25T20:00:00");

			Long teamBId = Long.valueOf(2);
			Event event = Event.builder().eventDateTime(eventDateTime).name(name).teamAId(incorrectTeamId)
					.teamBId(teamBId).build();

			eventService.createEvent(event);

		});
	}

}
