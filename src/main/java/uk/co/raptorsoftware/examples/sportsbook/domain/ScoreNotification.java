package uk.co.raptorsoftware.examples.sportsbook.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ScoreNotification {

	private LocalDateTime sendTime;

	private Long score;

	private Long teamId;

	private String teamName;

}
