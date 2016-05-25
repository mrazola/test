package com.king.test.service.ranking;

import java.util.List;

/**
 * Keeps an ordered list of {@code Record}
 *
 */
public interface Ranking {

	void insertScore(Record record);

	List<Record> getTop();

}