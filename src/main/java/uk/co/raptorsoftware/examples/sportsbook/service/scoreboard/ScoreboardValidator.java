package uk.co.raptorsoftware.examples.sportsbook.service.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import uk.co.raptorsoftware.examples.sportsbook.domain.Score;
import uk.co.raptorsoftware.examples.sportsbook.entities.EventEntity;

@Component
public class ScoreboardValidator {

	public void validateMandatoryParameters(Long eventId, Score score) {

		validateEventIdPresent(eventId);
		validateScorePresent(score);
	}

	private void validateEventIdPresent(Long eventId) {
		if (null == eventId) {
			throw new IllegalArgumentException("eventId is a mandatory parameter");
		}
	}

	private void validateScorePresent(Score score) {
		if (null == score) {
			throw new IllegalArgumentException("score is a mandatory parameter");
		}
	}

	public void validateScore(EventEntity event, Score score) {

		validateScoreValueIsPresent(score);
		validateTeamForScore(score);
		validateScoreTimeIsPresent(score);
		validateScoreTimeIsAfterEventDateTime(event, score);
		validateScoreIsForPlayingTeams(event, score);

	}

	private void validateScoreValueIsPresent(Score score) {
		if (null == score.getScore()) {
			throw new ScoreboardValidationException("Score must contain a score value");
		}
	}

	private void validateScoreTimeIsPresent(Score score) {
		if (null == score.getTimeRecorded()) {
			throw new ScoreboardValidationException(
					"Payload must contain a score time - please check your request values");
		}
	}

	private void validateScoreTimeIsAfterEventDateTime(EventEntity event, Score score) {
		if (null != score.getTimeRecorded() && score.getTimeRecorded().isBefore(event.getEventDateTime())) {
			throw new ScoreboardValidationException("Cannot record score time before the event starts");
		}
	}

	private void validateScoreIsForPlayingTeams(EventEntity event, Score score) {
		List<Long> validTeamIds = new ArrayList<>();
		if (null != event.getTeamA()) {
			validTeamIds.add(event.getTeamA().getId());
		}
		if (null != event.getTeamB()) {
			validTeamIds.add(event.getTeamB().getId());
		}

		if (!validTeamIds.contains(score.getTeamId())) {
			throw new ScoreboardValidationException("Cannot record a score for a team not playing in this event");
		}
	}

	public void validateTeamForScore(Score score) {
		if (null == score.getTeamId() || score.getTeamId() == 0) {
			throw new ScoreboardValidationException(
					"Payload must contain a valid teamId - please check your request values");
		}
	}
}
