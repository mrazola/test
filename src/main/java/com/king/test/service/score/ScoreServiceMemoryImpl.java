package com.king.test.service.score;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Basic in-memory implementation of {@code ScoreService}, as required by the test.
 * It won't provide any persistence.
 *
 */
public class ScoreServiceMemoryImpl implements ScoreService {

    private final ConcurrentMap<Integer, RankingLinkedListImpl> scoresByLevel;
    
    public ScoreServiceMemoryImpl() {
        this.scoresByLevel = new ConcurrentHashMap<>();
    }
    
    @Override
    public void insertScore(final Integer level, final Record record) {
        
        RankingLinkedListImpl forLevel = null;
        
        synchronized (scoresByLevel) {
            forLevel = scoresByLevel.get(level);
            if (forLevel == null) {
                forLevel = new RankingLinkedListImpl();
                scoresByLevel.put(level, forLevel);
            }
        }
        
        forLevel.insertScore(record);
    }

    @Override
    public List<Record> getHighScoreList(Integer level) {
        return scoresByLevel.get(level).getTop();
    }

}
