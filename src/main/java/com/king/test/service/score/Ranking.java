package com.king.test.service.score;

import java.util.List;

public interface Ranking {

	void insertScore(Record record);

	List<Record> getRank();

}