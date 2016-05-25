package com.king.test.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.king.test.service.ranking.Ranking;
import com.king.test.service.ranking.RankingLinkedListImpl;
import com.king.test.service.ranking.Record;
import com.king.test.service.session.Session;
import com.king.test.service.session.SessionService;

/**
 * Basic in-memory implementation of {@code ScoreService}, as required by the test.
 * It won't provide any persistence.
 *
 */
public class ScoreServiceMemoryImpl implements ScoreService {

	private final SessionService sessionService;
    private final ConcurrentMap<Integer, Ranking> scoresByLevel;
    
    public ScoreServiceMemoryImpl(SessionService sessionService) {
    	this.sessionService = sessionService;
        this.scoresByLevel = new ConcurrentHashMap<>();
    }
    

	@Override
	public String login(Integer uid) {
		// just delegate
		return sessionService.login(uid);
	}

    @Override
    public void insertScore(final Integer level, String sessionkey, Integer score) throws NoSuchElementException {
        
    	Integer uid = sessionService.getSession(sessionkey).orElseThrow(() -> new NoSuchElementException("Session not found"));
        Record record = new Record(uid, score);
        
        Ranking forLevel = null;
        
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
    	Ranking ranking = scoresByLevel.get(level);
        return ranking == null ? Collections.emptyList() : ranking.getTop();
    }

}
