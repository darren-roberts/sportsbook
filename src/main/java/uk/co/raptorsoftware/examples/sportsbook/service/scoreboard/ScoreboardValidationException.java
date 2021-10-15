package uk.co.raptorsoftware.examples.sportsbook.service.scoreboard;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ScoreboardValidationException extends RuntimeException {

	private static final long serialVersionUID = -8402070200774289756L;

	public ScoreboardValidationException(String msg) {
		super(msg);
	}

}
