package uk.co.raptorsoftware.examples.sportsbook.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Score {

	@ApiModelProperty(hidden = true)
	private Long id;

	private Long teamId;

	@Builder.Default
	private Long score = Long.valueOf(0);

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime timeRecorded;

}
