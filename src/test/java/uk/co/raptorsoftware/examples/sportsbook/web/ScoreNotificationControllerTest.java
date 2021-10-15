package uk.co.raptorsoftware.examples.sportsbook.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import uk.co.raptorsoftware.examples.sportsbook.service.scoreboard.notification.ScoreNotificationService;

@WebMvcTest(ScoresNotificationsController.class)
public class ScoreNotificationControllerTest extends BaseControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	ScoreNotificationService scoreNotificationService;

	@Test
	public void registerForNotification() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/scores/notification/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

}
