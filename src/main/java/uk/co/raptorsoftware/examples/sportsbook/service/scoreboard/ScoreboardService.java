package uk.co.raptorsoftware.examples.sportsbook.service.scoreboard;

import uk.co.raptorsoftware.examples.sportsbook.domain.LatestScore;
import uk.co.raptorsoftware.examples.sportsbook.domain.Score;

public interface ScoreboardService {

	public Score registerScore(Long eventId, Score score);

	public LatestScore findLatestScore(Long eventId);

}
