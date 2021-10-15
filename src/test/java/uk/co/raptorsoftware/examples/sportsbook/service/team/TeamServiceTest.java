package uk.co.raptorsoftware.examples.sportsbook.service.team;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.raptorsoftware.examples.sportsbook.domain.Team;
import uk.co.raptorsoftware.examples.sportsbook.entities.TeamEntity;
import uk.co.raptorsoftware.examples.sportsbook.persistence.TeamRepository;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private TeamService teamService = new TeamServiceImplementation();

	@Test
	void testSaveTeam() {

		String teamName = "Liverpool";
		Long teamId = Long.valueOf(1);
		TeamEntity savedTeamEntity = TeamEntity.builder().id(teamId).name(teamName).build();

		Team newTeam = Team.builder().name(teamName).build();

		when(teamRepository.save(Mockito.any(TeamEntity.class))).thenReturn(savedTeamEntity);

		Team returnTeam = teamService.createTeam(newTeam);

		assertNotNull(returnTeam);
		assertEquals(teamName, returnTeam.getName());
		assertNotNull(returnTeam.getId());

	}

	@Test
	void testCreateTeamWithMissingTeamNameThrowsValidationException() {
		Assertions.assertThrows(TeamValidationException.class, () -> {

			Team team = Team.builder().build();
			teamService.createTeam(team);
		});
	}

	@Test
	void testCreateTeamWithEmptyTeamThrowsValidationException() {
		Assertions.assertThrows(TeamValidationException.class, () -> {

			Team team = Team.builder().name("").build();

			teamService.createTeam(team);

		});
	}

	@Test
	void testCreateTeamWithNullTeamThrowsValidationException() {
		Assertions.assertThrows(TeamValidationException.class, () -> {

			Team team = null;

			teamService.createTeam(team);

		});
	}

	@Test
	void testCreateTeamWithInvalidTeamNameThrowsValidationException() {
		Assertions.assertThrows(TeamValidationException.class, () -> {

			Team team = Team.builder().name("TK").build();

			teamService.createTeam(team);

		});
	}

}
