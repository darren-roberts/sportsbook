package uk.co.raptorsoftware.examples.sportsbook.service.scoreboard;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.raptorsoftware.examples.sportsbook.domain.LatestScore;
import uk.co.raptorsoftware.examples.sportsbook.domain.Score;
import uk.co.raptorsoftware.examples.sportsbook.domain.ScoreNotification;
import uk.co.raptorsoftware.examples.sportsbook.entities.EventEntity;
import uk.co.raptorsoftware.examples.sportsbook.entities.ScoreEntity;
import uk.co.raptorsoftware.examples.sportsbook.entities.TeamEntity;
import uk.co.raptorsoftware.examples.sportsbook.persistence.EventRepository;
import uk.co.raptorsoftware.examples.sportsbook.persistence.ScoreRepository;
import uk.co.raptorsoftware.examples.sportsbook.persistence.TeamRepository;
import uk.co.raptorsoftware.examples.sportsbook.service.scoreboard.notification.ScoreNotificationService;

@Service
@Transactional
public class ScoreboardServiceImplementation implements ScoreboardService {

	Logger logger = LoggerFactory.getLogger(ScoreboardServiceImplementation.class);

	@Autowired
	private ScoreRepository scoreRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private ScoreNotificationService scoreNotificationService;

	@Override
	public Score registerScore(Long eventId, Score score) {

		if (null == eventId) {
			throw new IllegalArgumentException("eventId is a mandatory parameter");
		}

		if (null == score) {
			throw new IllegalArgumentException("score is a mandatory parameter");
		}

		Score savedScore = null;
		Optional<EventEntity> eventEntity = eventRepository.findById(eventId);
		if (eventEntity.isPresent()) {
			EventEntity event = eventEntity.get();

			validateScore(event, score);

			Optional<TeamEntity> teamEntity = teamRepository.findById(score.getTeamId());
			TeamEntity team = teamEntity.get();

			ScoreEntity scoreEntity = ScoreEntity.builder().team(team).score(score.getScore())
					.timeRecorded(score.getTimeRecorded()).build();
			scoreEntity.setEvent(event);

			scoreEntity = scoreRepository.save(scoreEntity);

			savedScore = Score.builder().id(scoreEntity.getId()).score(score.getScore())
					.timeRecorded(score.getTimeRecorded()).teamId(score.getTeamId()).build();

			ScoreNotification scoreNotification = ScoreNotification.builder().score(score.getScore())
					.sendTime(score.getTimeRecorded()).teamId(team.getId()).teamName(team.getName()).build();
			scoreNotificationService.notify(scoreNotification);

			logger.debug("score created: {}", savedScore);

		} else {
			throw new EventNotFoundException("Cannot find event with an id of:" + eventId);
		}
		return savedScore;
	}

	private void validateScore(EventEntity event, Score score) {

		if (null == score.getTeamId() || score.getTeamId() == 0) {
			throw new ScoreboardValidationException(
					"Payload must contain a valid teamId - please check your request values");
		}

		if (null == score.getTimeRecorded()) {
			throw new ScoreboardValidationException(
					"Payload must contain a score time - please check your request values");
		}

		if (score.getTimeRecorded().isBefore(event.getEventDateTime())) {
			throw new ScoreboardValidationException("Cannot record score time before the event starts");
		}

		List<Long> validTeamIds = Arrays.asList(new Long[] { event.getTeamA().getId(), event.getTeamB().getId() });

		if (!validTeamIds.contains(score.getTeamId())) {
			throw new ScoreboardValidationException("Cannot record a score for a team not playing in this event");
		}

	}

	@Override
	public LatestScore findLatestScore(Long eventId) {

		LatestScore latestScore = null;

		Optional<EventEntity> eventEntity = eventRepository.findById(eventId);
		if (eventEntity.isPresent()) {
			EventEntity theEvent = eventEntity.get();

			if (null != theEvent.getScores() && theEvent.getScores().size() > 0) {
				latestScore = computeLatestScore(theEvent);
			} else {
				latestScore = LatestScore.builder().teamNameA(theEvent.getTeamA().getName()).teamAScore(Long.valueOf(0))
						.teamBScore(Long.valueOf(0)).teamNameB(theEvent.getTeamB().getName()).build();
			}

		} else {
			throw new EventNotFoundException("event with eventId:" + eventId + " does not exist");
		}

		return latestScore;
	}

	private LatestScore computeLatestScore(EventEntity theEvent) {
		LatestScore latestScore = null;

		Comparator<ScoreEntity> reverseComparator = (c1, c2) -> {
			return c1.getTimeRecorded().compareTo(c2.getTimeRecorded());
		};

		ScoreEntity teamALatestScore = theEvent.getScores().stream()
				.filter(t -> t.getTeam().getId().equals(theEvent.getTeamA().getId())).sorted(reverseComparator)
				.reduce((a, b) -> b).orElse(null);

		ScoreEntity teamBLatestScore = theEvent.getScores().stream()
				.filter(t -> t.getTeam().getId().equals(theEvent.getTeamB().getId())).sorted(reverseComparator)
				.reduce((a, b) -> b).orElse(null);

		latestScore = LatestScore.builder().teamNameA(theEvent.getTeamA().getName()).teamAScore(Long.valueOf(0))
				.teamNameB(theEvent.getTeamB().getName()).teamBScore(Long.valueOf(0)).build();

		if (null != teamALatestScore) {
			latestScore.setTeamAScore(teamALatestScore.getScore());
			logger.debug("latestScore for teamA:{}", teamALatestScore.toString());
		}
		if (null != teamBLatestScore) {
			latestScore.setTeamBScore(teamBLatestScore.getScore());
			logger.debug("latestScore for teamB:{}", teamBLatestScore.toString());
		}

		latestScore.setLatestScoreDateTime(findLatestScoreDateTime(teamALatestScore, teamBLatestScore));

		return latestScore;
	}

	private LocalDateTime findLatestScoreDateTime(ScoreEntity teamALatestScore, ScoreEntity teamBLatestScore) {
		LocalDateTime latestScoreTime = null;
		if (null != teamALatestScore && null != teamBLatestScore) {

			if (teamALatestScore.getTimeRecorded().isAfter(teamBLatestScore.getTimeRecorded())) {
				latestScoreTime = teamALatestScore.getTimeRecorded();
			} else {
				latestScoreTime = teamBLatestScore.getTimeRecorded();
			}
		} else if (null == teamBLatestScore) {
			latestScoreTime = teamALatestScore.getTimeRecorded();

		} else if (null == teamALatestScore) {
			latestScoreTime = teamBLatestScore.getTimeRecorded();
		}

		return latestScoreTime;
	}

}
