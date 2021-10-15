package uk.co.raptorsoftware.examples.sportsbook.service.scoreboard.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import uk.co.raptorsoftware.examples.sportsbook.domain.ScoreNotification;

@ExtendWith(MockitoExtension.class)
public class ScoreNotificationServiceTest {

	private ScoreNotificationService scoreNotificationService = new ScoreNotificationServiceImplementation();

	@Mock
	private static SseEmitter sseEmitter;

	@Test
	public void testNotificationSent() throws Exception {

		scoreNotificationService.addEmitter(sseEmitter);

		ScoreNotification scoreNotification = ScoreNotification.builder().build();
		scoreNotificationService.notify(scoreNotification);

		Mockito.verify(sseEmitter, Mockito.times(1)).send(Mockito.any(SseEmitter.event().getClass()));

	}

}
