package com.king.test.service.ranking;

import java.util.List;

public interface Ranking {

	void insertScore(Record record);

	List<Record> getTop();

}