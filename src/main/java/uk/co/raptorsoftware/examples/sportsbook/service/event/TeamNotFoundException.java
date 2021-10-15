package uk.co.raptorsoftware.examples.sportsbook.service.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TeamNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 9198462982966612726L;

	public TeamNotFoundException(String message) {
		super(message);
	}

}
