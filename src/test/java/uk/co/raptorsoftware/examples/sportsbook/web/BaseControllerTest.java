package uk.co.raptorsoftware.examples.sportsbook.web;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	protected String asJsonString(final Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
