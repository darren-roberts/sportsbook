package uk.co.raptorsoftware.examples.sportsbook.service.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventValidationException extends RuntimeException {

	private static final long serialVersionUID = -3929197208677930371L;

	public EventValidationException(String message) {
		super(message);
	}

}
