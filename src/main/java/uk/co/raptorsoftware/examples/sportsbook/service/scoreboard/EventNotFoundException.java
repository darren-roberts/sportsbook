package uk.co.raptorsoftware.examples.sportsbook.service.scoreboard;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6370120177654923606L;

	public EventNotFoundException(String message) {
		super(message);

	}

}
