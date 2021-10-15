package uk.co.raptorsoftware.examples.sportsbook.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import uk.co.raptorsoftware.examples.sportsbook.domain.Team;
import uk.co.raptorsoftware.examples.sportsbook.service.team.TeamService;

@WebMvcTest(TeamsController.class)
public class TeamControllerTest extends BaseControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	TeamService teamService;

	@Test
	public void createTeam() throws Exception {

		String teamName = "Liverpool";
		Team team = Team.builder().name(teamName).build();
		Team savedTeam = Team.builder().id(Long.valueOf(1)).name(teamName).build();
		when(teamService.createTeam(team)).thenReturn(savedTeam);

		mvc.perform(MockMvcRequestBuilders.post("/api/teams/").content(asJsonString(team))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print()).andReturn();
	}

}
