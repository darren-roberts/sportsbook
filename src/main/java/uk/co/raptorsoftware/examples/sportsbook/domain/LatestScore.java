package uk.co.raptorsoftware.examples.sportsbook.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LatestScore {

	private String teamNameA;
	private Long teamAScore;
	private String teamNameB;
	private Long teamBScore;

	private LocalDateTime latestScoreDateTime;

	@JsonProperty
	public String getFormattedScore() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(teamNameA);
		buffer.append(" ");
		buffer.append(teamAScore);
		buffer.append(" - ");
		buffer.append(teamNameB);
		buffer.append(" ");
		buffer.append(teamBScore);

		return buffer.toString();
	}

}
