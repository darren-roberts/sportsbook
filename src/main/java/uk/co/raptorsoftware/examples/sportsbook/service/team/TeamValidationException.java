package uk.co.raptorsoftware.examples.sportsbook.service.team;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TeamValidationException extends RuntimeException {

	private static final long serialVersionUID = -3970459106897282589L;

	public TeamValidationException(String msg) {
		super(msg);
	}

}
