package com.king.test.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.king.test.service.ranking.Record;
import com.king.test.service.session.SessionServiceCacheImpl;

/**
 * Unit tests for {@code ScoreService}
 *
 */
public class TestScoreService {

	ScoreService scoreService;
	
	@Before
	public void setUp() {
		this.scoreService = new ScoreServiceMemoryImpl(new SessionServiceCacheImpl());
	}
	
    @Test(expected=IllegalArgumentException.class)
    public void testUidValidation() {
        scoreService.login(-3);
    }
	
    @Test
    public void basicScoreServiceTest() {

        // level 1
    	Integer level = 1;
	    this.insertNewScore(1, level, 1000);
	    this.insertNewScore(2, level, 1400);
	    this.insertNewScore(3, level, 1200);
	    
	    List<Record> level1 = scoreService.getHighScoreList(1);
	    Assert.assertEquals(3, level1.size());
	    Assert.assertEquals(Integer.valueOf(2), level1.get(0).getUid()); // first
        Assert.assertEquals(Integer.valueOf(1), level1.get(2).getUid()); // last
	    
        // level 2
        // same score, to check that oldest entries remain on top (one of the assumptions)
	    level = 2;
	    this.insertNewScore(10, level, 1000);
	    this.insertNewScore(20, level, 1000);
	    this.insertNewScore(30, level, 1000);

        List<Record> level2 = scoreService.getHighScoreList(2);

        // check sizes
        Assert.assertEquals(3, level2.size());
        // check order
        Assert.assertEquals(Integer.valueOf(10), level2.get(0).getUid());
        Assert.assertEquals(Integer.valueOf(20), level2.get(1).getUid());
        Assert.assertEquals(Integer.valueOf(30), level2.get(2).getUid());

    }
    
    @Test
    public void testEmptyScoreList() {
    	List<Record> level40 = scoreService.getHighScoreList(40);
    	Assert.assertTrue(level40.isEmpty());
    }
    
    private void insertNewScore(Integer uid, Integer level, Integer score) {
    	String sessionkey = scoreService.login(uid);
    	scoreService.insertScore(level, sessionkey, score);
    }

}
