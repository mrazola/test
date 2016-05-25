package com.king.test.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.king.test.service.score.ScoreServiceMemoryImpl;
import com.king.test.service.session.SessionServiceCacheImpl;

/**
 * Unit tests for {@code RequestDispatcher}
 *
 */
public class TestRequestDispatcher {

	RequestDispatcher realDispatcher;
	
	@Before
	public void setUp() {
		realDispatcher = new RequestDispatcher(new SessionServiceCacheImpl(),
				new ScoreServiceMemoryImpl());
	}
	
	/**
	 * Tests a full flow from the {@code RequestDispatcher}
	 */
	@Test
	public void fullFlowTest() {

		this.insertScore(1, 10, 1200);
		this.insertScore(2, 10, 1100);
		this.insertScore(3, 10, 1500);
		
		ClientRequest highscore = ClientRequest.builder()
				.withAction("highscorelist")
				.withId(10)
				.build();
		
		String result = realDispatcher.handle(highscore).get();
		
		Assert.assertEquals("3=1500,1=1200,2=1100", result);
	}
	
	@Test
	public void testEmptyHighScores() {
		ClientRequest highscoreEmpty = ClientRequest.builder()
				.withAction("highscorelist")
				.withId(12)
				.build();
		
		String resultEmpty = realDispatcher.handle(highscoreEmpty).get();
		
		Assert.assertEquals("", resultEmpty);
	}
	
	@Test
	public void dispatchingTest() throws URISyntaxException, IOException {

		MockedHttpExchange httpExchange = new MockedHttpExchange();

		httpExchange.reset(new URI("http://localhost:8080/fooo"), "");

		realDispatcher.handle(httpExchange);
		Assert.assertEquals(400, httpExchange.getResponseCode());

		httpExchange.reset(new URI("http://localhost:8080/1/login"), "");
		realDispatcher.handle(httpExchange);
		Assert.assertEquals(200, httpExchange.getResponseCode());

	}
	
	private void insertScore(Integer uid, Integer level, Integer score) {
		ClientRequest login = ClientRequest.builder().withAction("login").withId(uid).build();
		String sessionkey = realDispatcher.handle(login).orElseThrow(() -> new IllegalStateException("Login failed"));

		ClientRequest scoreRequest = ClientRequest.builder()
				.withAction("score")
				.withId(level) // level
				.addParam("sessionkey", sessionkey)
				.withBody(score.toString())
				.build();

		realDispatcher.handle(scoreRequest);
	}
}
