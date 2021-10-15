package uk.co.raptorsoftware.examples.sportsbook.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel
public class Team {

	@ApiModelProperty(hidden = true)
	private Long id;

	private String name;

}
