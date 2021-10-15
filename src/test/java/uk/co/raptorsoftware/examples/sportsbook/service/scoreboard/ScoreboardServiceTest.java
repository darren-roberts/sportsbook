package uk.co.raptorsoftware.examples.sportsbook.service.scoreboard;

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

import uk.co.raptorsoftware.examples.sportsbook.domain.LatestScore;
import uk.co.raptorsoftware.examples.sportsbook.domain.Score;
import uk.co.raptorsoftware.examples.sportsbook.entities.EventEntity;
import uk.co.raptorsoftware.examples.sportsbook.entities.ScoreEntity;
import uk.co.raptorsoftware.examples.sportsbook.entities.TeamEntity;
import uk.co.raptorsoftware.examples.sportsbook.persistence.EventRepository;
import uk.co.raptorsoftware.examples.sportsbook.persistence.ScoreRepository;
import uk.co.raptorsoftware.examples.sportsbook.persistence.TeamRepository;
import uk.co.raptorsoftware.examples.sportsbook.service.scoreboard.notification.ScoreNotificationService;

@ExtendWith(MockitoExtension.class)
public class ScoreboardServiceTest {

	@Mock
	private TeamRepository teamRepository;

	@Mock
	private EventRepository eventRepository;

	@Mock
	private ScoreRepository scoreRepository;

	@Mock
	private ScoreNotificationService scoreNotificationService;

	@Mock
	private ScoreboardValidator scoreboardValidator;

	@InjectMocks
	private ScoreboardService scoreboardService = new ScoreboardServiceImplementation();

	@Test
	void testRegisterScore() {

		Long eventId = Long.valueOf(1);

		Long scoreValue = Long.valueOf(1);

		Long teamAId = Long.valueOf(1);
		String teamAName = "Liverpool";
		TeamEntity teamAEntity = TeamEntity.builder().id(teamAId).name(teamAName).build();
		Optional<TeamEntity> teamA = Optional.of(teamAEntity);
		when(teamRepository.findById(teamAId)).thenReturn(teamA);

		Long teamBId = Long.valueOf(2);
		String teamBName = "Luton";
		TeamEntity teamBEntity = TeamEntity.builder().id(teamBId).name(teamBName).build();

		List<ScoreEntity> scores = new ArrayList<ScoreEntity>();
		LocalDateTime eventDateTime = LocalDateTime.parse("2019-01-25T20:00:00");
		EventEntity eventEntity = EventEntity.builder().id(eventId).scores(scores).teamA(teamAEntity).teamB(teamBEntity)
				.eventDateTime(eventDateTime).build();
		Optional<EventEntity> event = Optional.of(eventEntity);

		when(eventRepository.findById(eventId)).thenReturn(event);

		LocalDateTime scoreTime = LocalDateTime.parse("2019-01-25T20:21:00");
		Score score = Score.builder().teamId(teamAId).score(scoreValue).timeRecorded(scoreTime).build();

		ScoreEntity scoreEntity = ScoreEntity.builder().timeRecorded(scoreTime).score(Long.valueOf(1)).team(teamAEntity)
				.event(eventEntity).build();

		when(scoreRepository.save(Mockito.any(ScoreEntity.class))).thenReturn(scoreEntity);

		Score returnedScore = scoreboardService.registerScore(eventId, score);

		assertNotNull(returnedScore);
		assertEquals(scoreValue, returnedScore.getScore());

	}

	@Test
	void testFindLatestScoreAsDraw() {

		Long eventId = Long.valueOf(1);
		List<ScoreEntity> scores = new ArrayList<ScoreEntity>();
		EventEntity eventEntity = EventEntity.builder().id(eventId).scores(scores).build();
		Optional<EventEntity> event = Optional.of(eventEntity);

		Long teamAId = Long.valueOf(1);
		String teamAName = "Liverpool";
		TeamEntity teamAEntity = TeamEntity.builder().id(teamAId).name(teamAName).build();
		Optional<TeamEntity> teamA = Optional.of(teamAEntity);
		eventEntity.setTeamA(teamA.get());

		Long teamBId = Long.valueOf(1);
		String teamBName = "Luton";
		TeamEntity teamBEntity = TeamEntity.builder().id(teamBId).name(teamBName).build();
		Optional<TeamEntity> teamB = Optional.of(teamBEntity);
		eventEntity.setTeamB(teamB.get());

		LocalDateTime scoreTime1 = LocalDateTime.parse("2019-01-25T20:21:00");
		Long teamAScore = Long.valueOf(1);
		ScoreEntity score1 = ScoreEntity.builder().team(teamAEntity).event(eventEntity).score(Long.valueOf(1))
				.timeRecorded(scoreTime1).build();
		scores.add(score1);

		LocalDateTime scoreTime2 = LocalDateTime.parse("2019-01-25T20:23:00");
		Long teamBScore = Long.valueOf(1);
		ScoreEntity score2 = ScoreEntity.builder().team(teamBEntity).event(eventEntity).score(teamBScore)
				.timeRecorded(scoreTime2).build();
		scores.add(score2);

		when(eventRepository.findById(eventId)).thenReturn(event);

		LatestScore latestScore = scoreboardService.findLatestScore(eventId);

		assertNotNull(latestScore);
		assertEquals(teamAScore, latestScore.getTeamAScore());
		assertEquals(teamBScore, latestScore.getTeamBScore());
		assertEquals(teamAName, latestScore.getTeamNameA());
		assertEquals(teamBName, latestScore.getTeamNameB());
		assertEquals(scoreTime2, latestScore.getLatestScoreDateTime());
		assertEquals("Liverpool 1 - Luton 1", latestScore.getFormattedScore());

	}

	@Test
	void testFindLatestScoreAsTeamAWinsTwoOne() {

		Long eventId = Long.valueOf(1);
		List<ScoreEntity> scores = new ArrayList<ScoreEntity>();
		EventEntity eventEntity = EventEntity.builder().id(eventId).scores(scores).build();
		Optional<EventEntity> event = Optional.of(eventEntity);

		String teamAName = "Liverpool";
		TeamEntity teamAEntity = TeamEntity.builder().id(Long.valueOf(1)).name(teamAName).build();
		Optional<TeamEntity> teamA = Optional.of(teamAEntity);
		eventEntity.setTeamA(teamA.get());

		String teamBName = "Luton";
		TeamEntity teamBEntity = TeamEntity.builder().id(Long.valueOf(2)).name(teamBName).build();
		Optional<TeamEntity> teamB = Optional.of(teamBEntity);
		eventEntity.setTeamB(teamB.get());

		LocalDateTime scoreTime1 = LocalDateTime.parse("2019-01-25T20:21:00");
		Long teamAScore1 = Long.valueOf(1);
		ScoreEntity score1 = ScoreEntity.builder().team(teamAEntity).event(eventEntity).score(teamAScore1)
				.timeRecorded(scoreTime1).build();
		scores.add(score1);
		LocalDateTime scoreTime2 = LocalDateTime.parse("2019-01-25T20:35:00");
		Long teamAScore2 = Long.valueOf(2);
		ScoreEntity score2 = ScoreEntity.builder().team(teamAEntity).event(eventEntity).score(teamAScore2)
				.timeRecorded(scoreTime2).build();
		scores.add(score2);

		LocalDateTime scoreTime3 = LocalDateTime.parse("2019-01-25T20:23:00");
		Long teamBScore = Long.valueOf(1);
		ScoreEntity score3 = ScoreEntity.builder().team(teamBEntity).event(eventEntity).score(teamBScore)
				.timeRecorded(scoreTime3).build();
		scores.add(score3);

		when(eventRepository.findById(eventId)).thenReturn(event);

		LatestScore latestScore = scoreboardService.findLatestScore(eventId);

		assertNotNull(latestScore);
		assertEquals(teamAScore2, latestScore.getTeamAScore());
		assertEquals(teamBScore, latestScore.getTeamBScore());
		assertEquals(teamAName, latestScore.getTeamNameA());
		assertEquals(teamBName, latestScore.getTeamNameB());
		assertEquals(scoreTime2, latestScore.getLatestScoreDateTime());
		assertEquals("Liverpool 2 - Luton 1", latestScore.getFormattedScore());

	}

	@Test
	void testFindLatestScoreAsTeamAWinsOneNil() {

		Long eventId = Long.valueOf(1);
		List<ScoreEntity> scores = new ArrayList<ScoreEntity>();
		EventEntity eventEntity = EventEntity.builder().id(eventId).scores(scores).build();
		Optional<EventEntity> event = Optional.of(eventEntity);

		String teamAName = "Liverpool";
		TeamEntity teamAEntity = TeamEntity.builder().id(Long.valueOf(1)).name(teamAName).build();
		Optional<TeamEntity> teamA = Optional.of(teamAEntity);
		eventEntity.setTeamA(teamA.get());

		String teamBName = "Luton";
		TeamEntity teamBEntity = TeamEntity.builder().id(Long.valueOf(2)).name(teamBName).build();
		Optional<TeamEntity> teamB = Optional.of(teamBEntity);
		eventEntity.setTeamB(teamB.get());

		LocalDateTime scoreTime1 = LocalDateTime.parse("2019-01-25T20:21:00");
		Long teamAScore1 = Long.valueOf(1);
		ScoreEntity score1 = ScoreEntity.builder().team(teamAEntity).event(eventEntity).score(teamAScore1)
				.timeRecorded(scoreTime1).build();
		scores.add(score1);

		when(eventRepository.findById(eventId)).thenReturn(event);

		LatestScore latestScore = scoreboardService.findLatestScore(eventId);

		assertNotNull(latestScore);
		assertEquals(teamAScore1, latestScore.getTeamAScore());
		assertEquals(0, latestScore.getTeamBScore());
		assertEquals(teamAName, latestScore.getTeamNameA());
		assertEquals(teamBName, latestScore.getTeamNameB());
		assertEquals(scoreTime1, latestScore.getLatestScoreDateTime());
		assertEquals("Liverpool 1 - Luton 0", latestScore.getFormattedScore());

	}

	@Test
	void testFindLatestScoreAsTeamBWinsOneNil() {

		Long eventId = Long.valueOf(1);
		List<ScoreEntity> scores = new ArrayList<ScoreEntity>();
		EventEntity eventEntity = EventEntity.builder().id(eventId).scores(scores).build();
		Optional<EventEntity> event = Optional.of(eventEntity);

		String teamAName = "Liverpool";
		TeamEntity teamAEntity = TeamEntity.builder().id(Long.valueOf(1)).name(teamAName).build();
		Optional<TeamEntity> teamA = Optional.of(teamAEntity);
		eventEntity.setTeamA(teamA.get());

		String teamBName = "Luton";
		TeamEntity teamBEntity = TeamEntity.builder().id(Long.valueOf(2)).name(teamBName).build();
		Optional<TeamEntity> teamB = Optional.of(teamBEntity);
		eventEntity.setTeamB(teamB.get());

		LocalDateTime scoreTime1 = LocalDateTime.parse("2019-01-25T20:21:00");
		Long teamBScore1 = Long.valueOf(1);
		ScoreEntity score1 = ScoreEntity.builder().team(teamBEntity).event(eventEntity).score(teamBScore1)
				.timeRecorded(scoreTime1).build();
		scores.add(score1);

		when(eventRepository.findById(eventId)).thenReturn(event);

		LatestScore latestScore = scoreboardService.findLatestScore(eventId);

		assertNotNull(latestScore);
		assertEquals(teamBScore1, latestScore.getTeamBScore());
		assertEquals(0, latestScore.getTeamAScore());
		assertEquals(teamAName, latestScore.getTeamNameA());
		assertEquals(teamBName, latestScore.getTeamNameB());
		assertEquals(scoreTime1, latestScore.getLatestScoreDateTime());
		assertEquals("Liverpool 0 - Luton 1", latestScore.getFormattedScore());

	}

	@Test
	void testFindLatestScoreWithNoScores() {
		Long eventId = Long.valueOf(1);
		EventEntity eventEntity = EventEntity.builder().id(eventId).build();
		Optional<EventEntity> event = Optional.of(eventEntity);

		String teamAName = "Liverpool";
		TeamEntity teamAEntity = TeamEntity.builder().id(Long.valueOf(1)).name(teamAName).build();
		Optional<TeamEntity> teamA = Optional.of(teamAEntity);
		eventEntity.setTeamA(teamA.get());

		String teamBName = "Luton";
		TeamEntity teamBEntity = TeamEntity.builder().id(Long.valueOf(2)).name(teamBName).build();
		Optional<TeamEntity> teamB = Optional.of(teamBEntity);
		eventEntity.setTeamB(teamB.get());

		when(eventRepository.findById(eventId)).thenReturn(event);

		LatestScore latestScore = scoreboardService.findLatestScore(eventId);

		assertNotNull(latestScore);
		assertEquals(0, latestScore.getTeamAScore());
		assertEquals(0, latestScore.getTeamBScore());
		assertEquals(teamAName, latestScore.getTeamNameA());
		assertEquals(teamBName, latestScore.getTeamNameB());
		assertEquals(null, latestScore.getLatestScoreDateTime());
		assertEquals("Liverpool 0 - Luton 0", latestScore.getFormattedScore());
	}

	@Test
	void testFindLatestScoreWithEmptyScores() {
		Long eventId = Long.valueOf(1);
		List<ScoreEntity> scores = new ArrayList<ScoreEntity>();
		EventEntity eventEntity = EventEntity.builder().id(eventId).scores(scores).build();
		Optional<EventEntity> event = Optional.of(eventEntity);

		String teamAName = "Liverpool";
		TeamEntity teamAEntity = TeamEntity.builder().id(Long.valueOf(1)).name(teamAName).build();
		Optional<TeamEntity> teamA = Optional.of(teamAEntity);
		eventEntity.setTeamA(teamA.get());

		String teamBName = "Luton";
		TeamEntity teamBEntity = TeamEntity.builder().id(Long.valueOf(2)).name(teamBName).build();
		Optional<TeamEntity> teamB = Optional.of(teamBEntity);
		eventEntity.setTeamB(teamB.get());

		when(eventRepository.findById(eventId)).thenReturn(event);

		LatestScore latestScore = scoreboardService.findLatestScore(eventId);

		assertNotNull(latestScore);
		assertEquals(0, latestScore.getTeamAScore());
		assertEquals(0, latestScore.getTeamBScore());
		assertEquals(teamAName, latestScore.getTeamNameA());
		assertEquals(teamBName, latestScore.getTeamNameB());
		assertEquals(null, latestScore.getLatestScoreDateTime());
		assertEquals("Liverpool 0 - Luton 0", latestScore.getFormattedScore());
	}

}
