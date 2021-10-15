package uk.co.raptorsoftware.examples.sportsbook.domain;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Event {

	@ApiModelProperty(hidden = true)
	private Long id;

	private String name;

	private LocalDateTime eventDateTime;

	@ApiModelProperty(hidden = true)
	private List<Score> scores;

	private Long teamAId;

	private Long teamBId;

}
