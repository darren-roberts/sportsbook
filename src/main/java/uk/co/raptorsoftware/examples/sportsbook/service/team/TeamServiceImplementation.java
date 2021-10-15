package uk.co.raptorsoftware.examples.sportsbook.service.team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.raptorsoftware.examples.sportsbook.domain.Team;
import uk.co.raptorsoftware.examples.sportsbook.entities.TeamEntity;
import uk.co.raptorsoftware.examples.sportsbook.persistence.TeamRepository;

@Service
public class TeamServiceImplementation implements TeamService {

	Logger logger = LoggerFactory.getLogger(TeamServiceImplementation.class);

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public Team createTeam(Team newTeam) {

		validateTeam(newTeam);

		TeamEntity teamEntity = TeamEntity.builder().name(newTeam.getName()).build();

		teamEntity = teamRepository.save(teamEntity);

		newTeam.setId(teamEntity.getId());
		
		logger.debug("New team created:{}", newTeam);

		return newTeam;

	}

	private void validateTeam(Team team) {
		if (null == team) {
			throw new TeamValidationException(
					"request does not contain a valid Team - please check your request payload");
		}
		if (null == team.getName() || team.getName().isEmpty() || team.getName().length() < 3) {
			throw new TeamValidationException(
					"Payload must contain a valid team name, minimum of 3 characters, - please check your request values / payload");
		}
	}

}
