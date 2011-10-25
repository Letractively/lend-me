package com.lendme.utils;

import java.util.Comparator;

import com.lendme.entities.User;

public class UserRankingComparatorStrategy implements Comparator<User>{

	@Override
	public int compare(User o1, User o2) {
		
		int result = 0;
		if (o1.getScore() > o2.getScore()) {
			result = 1;
		} else if (o1.getScore() < o2.getScore()) {
			result = -1;
		}
		return result;
	}

}
