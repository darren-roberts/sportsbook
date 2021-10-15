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
import uk.co.raptorsoftware.examples.sportsbook.domain.Team;
import uk.co.raptorsoftware.examples.sportsbook.service.team.TeamService;

@RestController
public class TeamsController {

	Logger logger = LoggerFactory.getLogger(TeamsController.class);

	@Autowired
	private TeamService teamService;

	@ApiOperation(value = "createTeam", notes = "Use this end point to create a new team, team names must be unique", nickname = "createTeam")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 400, message = "bad request - please check your input values"),
			@ApiResponse(code = 201, message = "Successful creation of Team", response = Team.class) })

	@PostMapping(value = "/api/teams/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Team> createTeam(@RequestBody Team newTeam) {
		try {
			return new ResponseEntity<Team>(teamService.createTeam(newTeam), HttpStatus.CREATED);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
}
