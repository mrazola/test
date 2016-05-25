package com.king.test.service.score;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@code ScoreService}
 *
 */
public class TestScoreService {

	ScoreService scoreService;
	
	@Before
	public void setUp() {
		this.scoreService = new ScoreServiceMemoryImpl();
	}
	
    @Test
    public void basicRankingTest() {
        //ScoreService scoreService = new ScoreServiceMemoryImpl();

        // level 1
        scoreService.insertScore(1, new Record(1, 1000));
        scoreService.insertScore(1, new Record(2, 2000));
        scoreService.insertScore(1, new Record(3, 1500));
        scoreService.insertScore(1, new Record(4, 1100));

        // level 2
        // same score, to check that oldest entries remain on top (one of the assumptions)
        scoreService.insertScore(2, new Record(1, 1000));
        scoreService.insertScore(2, new Record(2, 1000));
        scoreService.insertScore(2, new Record(5, 1000));

        List<Record> level1 = scoreService.getHighScoreList(1);
        List<Record> level2 = scoreService.getHighScoreList(2);

        // check sizes
        Assert.assertEquals(4, level1.size());
        Assert.assertEquals(3, level2.size());

        // check order
        Assert.assertEquals(Integer.valueOf(2), level1.get(0).getUid());
        Assert.assertEquals(Integer.valueOf(3), level1.get(1).getUid());
        Assert.assertEquals(Integer.valueOf(4), level1.get(2).getUid());
        Assert.assertEquals(Integer.valueOf(1), level1.get(3).getUid());

        Assert.assertEquals(Integer.valueOf(1), level2.get(0).getUid());
        Assert.assertEquals(Integer.valueOf(2), level2.get(1).getUid());
        Assert.assertEquals(Integer.valueOf(5), level2.get(2).getUid());

    }

}
