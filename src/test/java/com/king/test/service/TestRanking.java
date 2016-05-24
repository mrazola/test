/*
 * Copyright (c) 2016 Ubisoft Mobile.
 * This program is not published or distributed in source code form; accordingly,
 * this source code is and remains confidential to and a trade secret of
 * Ubisoft Mobile.
 *
 * All rights reserved.
 */
package com.king.test.service;

import org.junit.Test;

/**
 * TODO Comment
 *
 * @author mrazola
 * @created 24 May 2016
 */
public class TestRanking {

    
    @Test
    public void quickTest() {
        RankingReversed ranking = new RankingReversed();
        
        ranking.insertScore(new Record(1, 1000));
        ranking.insertScore(new Record(2, 2000));
        ranking.insertScore(new Record(3, 1500));
        ranking.insertScore(new Record(4, 1100));
        ranking.insertScore(new Record(5, 1700));
        ranking.insertScore(new Record(6, 1300));
        ranking.insertScore(new Record(5, 1300));
        ranking.insertScore(new Record(5, 1750));
        ranking.insertScore(new Record(3, 1800));
        
        System.out.println(ranking.getRank());
        
    }
    
    
    @Test
    public void repeatedPlayerTest() {
        RankingReversed ranking = new RankingReversed();
        
        System.out.println(ranking.getRank());
        
    }
}
