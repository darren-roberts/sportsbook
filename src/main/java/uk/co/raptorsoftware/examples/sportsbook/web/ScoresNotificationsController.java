package uk.co.raptorsoftware.examples.sportsbook.web;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.co.raptorsoftware.examples.sportsbook.service.scoreboard.notification.ScoreNotificationService;

@RestController
@CrossOrigin
public class ScoresNotificationsController {

	Logger logger = LoggerFactory.getLogger(ScoresNotificationsController.class);

	final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	@Autowired
	private ScoreNotificationService scoreNotificationService;

	@ApiOperation(value = "notifyScores", notes = "use this API to register for notifications or new scores", nickname = "registerNotifications")
	@ApiResponses(value = @ApiResponse(code = 200, message = "Successful registration of notifications", response = SseEmitter.class))
	@GetMapping("/api/scores/notification")
	public ResponseEntity<SseEmitter> notifyScores() throws InterruptedException, IOException {
		final SseEmitter emitter = new SseEmitter();
		try {
			scoreNotificationService.addEmitter(emitter);
			emitter.onCompletion(() -> scoreNotificationService.removeEmitter(emitter));
			emitter.onTimeout(() -> scoreNotificationService.removeEmitter(emitter));
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(emitter, HttpStatus.OK);
	}
}
