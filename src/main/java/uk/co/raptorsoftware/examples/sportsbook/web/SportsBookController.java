package uk.co.raptorsoftware.examples.sportsbook.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.co.raptorsoftware.examples.sportsbook.domain.LatestScore;
import uk.co.raptorsoftware.examples.sportsbook.domain.Score;
import uk.co.raptorsoftware.examples.sportsbook.service.scoreboard.EventNotFoundException;
import uk.co.raptorsoftware.examples.sportsbook.service.scoreboard.ScoreboardService;

@RestController
public class SportsBookController {

	Logger logger = LoggerFactory.getLogger(SportsBookController.class);

	@Autowired
	private ScoreboardService scoreBoardService;

	@GetMapping(value = "/api/scoreboards/{eventId}/scores/latest", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LatestScore> getLatestScore(@PathVariable("eventId") Long eventId) {
		try {
			return new ResponseEntity<LatestScore>(scoreBoardService.findLatestScore(eventId), HttpStatus.OK);
		} catch (EventNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found", e);
		}
	}

	@ApiOperation(value = "saveScore", notes = "Use this end point to create score a secore for an event.The teams and event must be created first, and approrpiate teamId must match the one of the teamIds associated with the event", nickname = "createScore")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 400, message = "bad request - please check your input values"),
			@ApiResponse(code = 201, message = "Successful creation of Score") })
	@PostMapping(value = "/api/scoreboards/{eventId}/scores/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void saveScore(@PathVariable("eventId") Long eventId, @RequestBody Score score) {
		try {
			scoreBoardService.registerScore(eventId, score);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
