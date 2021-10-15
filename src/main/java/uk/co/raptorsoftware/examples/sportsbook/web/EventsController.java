package uk.co.raptorsoftware.examples.sportsbook.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.co.raptorsoftware.examples.sportsbook.domain.Event;
import uk.co.raptorsoftware.examples.sportsbook.service.event.EventService;

@RestController
public class EventsController {

	Logger logger = LoggerFactory.getLogger(EventsController.class);

	@Autowired
	private EventService eventsService;

	@ApiOperation(value = "createEvent", notes = "Use this end point to create a new event. Teams should be setup using the createTeam API first", nickname = "createEvent")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 400, message = "bad request - please check your input values"),
			@ApiResponse(code = 201, message = "Successful creation of Event", response = Event.class) })
	@PostMapping(value = "/api/events/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Event> createEvent(@RequestBody Event newEvent) {
		try {
			return new ResponseEntity<Event>(eventsService.createEvent(newEvent), HttpStatus.CREATED);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

}
