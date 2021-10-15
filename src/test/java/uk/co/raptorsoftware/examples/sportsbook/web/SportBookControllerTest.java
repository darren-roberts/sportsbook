package uk.co.raptorsoftware.examples.sportsbook.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import uk.co.raptorsoftware.examples.sportsbook.domain.Event;
import uk.co.raptorsoftware.examples.sportsbook.domain.LatestScore;
import uk.co.raptorsoftware.examples.sportsbook.service.scoreboard.ScoreboardService;

@WebMvcTest(SportsBookController.class)
public class SportBookControllerTest extends BaseControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	ScoreboardService scoreboardService;

	@Test
	public void registerScore() throws Exception {
		Long teamAId = Long.valueOf(1);
		Long teamBId = Long.valueOf(1);
		LocalDateTime eventDateTime = LocalDateTime.parse("2019-01-25T20:00");
		Long eventId = Long.valueOf(1);
		Event event = Event.builder().id(eventId).eventDateTime(eventDateTime).name("TEST").teamAId(teamAId)
				.teamBId(teamBId).build();
		mvc.perform(MockMvcRequestBuilders.post("/api/scoreboards/" + eventId + "/scores/").content(asJsonString(event))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void getLatestScore() throws Exception {

		Long eventId = Long.valueOf(1);
		LocalDateTime latestScoreDateTime = LocalDateTime.parse("2019-01-25T20:00");
		LatestScore latestScore = LatestScore.builder().teamNameA("Liverpool").teamNameB("Luton").latestScoreDateTime(
				latestScoreDateTime)
				.teamAScore(Long.valueOf(1)).teamBScore(Long.valueOf(2)).build();
		mvc.perform(MockMvcRequestBuilders.get("/api/scoreboards/" + eventId + "/scores/latest")
				.content(asJsonString(latestScore)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
	}

}
