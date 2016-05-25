package com.king.test.service.ranking;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.king.test.service.ranking.Ranking;
import com.king.test.service.ranking.RankingLinkedListImpl;
import com.king.test.service.ranking.Record;

/**
 * Unit tests for {@code Ranking}
 *
 */
public class TestRanking {

    @Test
    public void testRankingMaxSize() {

        int size = 3;
        Ranking ranking = new RankingLinkedListImpl(size);

        ranking.insertScore(new Record(1, 1000));
        ranking.insertScore(new Record(2, 2000));
        ranking.insertScore(new Record(3, 1500));

        Assert.assertEquals(size, ranking.getTop().size());

        // insert a record with lower score than current
        ranking.insertScore(new Record(7, 900));
        Assert.assertEquals(size, ranking.getTop().size());

        // insert a record that would replace current lowest
        ranking.insertScore(new Record(7, 1100));
        Assert.assertEquals(size, ranking.getTop().size());

        // insert a record that would replace current highest
        ranking.insertScore(new Record(7, 3000));
        Assert.assertEquals(size, ranking.getTop().size());
    }

    @Test
    public void sameScoreTest() {
    	
    	Ranking ranking = new RankingLinkedListImpl();
    	// same score, to check that oldest entries remain on top (one of the assumptions)
    	ranking.insertScore(new Record(10, 1000));
    	ranking.insertScore(new Record(20, 1000));
    	ranking.insertScore(new Record(30, 1000));

        List<Record> top = ranking.getTop();
        Assert.assertEquals(top.size(), 3); // size must still be 6
        Assert.assertEquals(Integer.valueOf(10), top.get(0).getUid());
        Assert.assertEquals(Integer.valueOf(20), top.get(1).getUid());
        Assert.assertEquals(Integer.valueOf(30), top.get(2).getUid());
    }
    
    @Test
    public void updatingScoreTest() {
        Ranking ranking = new RankingLinkedListImpl();

        ranking.insertScore(new Record(1, 1000));
        ranking.insertScore(new Record(2, 2000));
        ranking.insertScore(new Record(3, 1500));
        ranking.insertScore(new Record(4, 1100));
        ranking.insertScore(new Record(5, 1700));
        ranking.insertScore(new Record(6, 1300));
        ranking.insertScore(new Record(5, 1300));
        ranking.insertScore(new Record(5, 1750));
        ranking.insertScore(new Record(3, 1800));

        List<Record> top = ranking.getTop();
        Assert.assertEquals(top.size(), 6); // size must still be 6
        Assert.assertEquals(Integer.valueOf(2), top.get(0).getUid());
        Assert.assertEquals(Integer.valueOf(3), top.get(1).getUid());
        Assert.assertEquals(Integer.valueOf(5), top.get(2).getUid());
        Assert.assertEquals(Integer.valueOf(6), top.get(3).getUid());
        Assert.assertEquals(Integer.valueOf(4), top.get(4).getUid());
        Assert.assertEquals(Integer.valueOf(1), top.get(5).getUid());
    }
}
