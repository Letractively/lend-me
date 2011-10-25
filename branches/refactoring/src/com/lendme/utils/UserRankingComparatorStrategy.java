package com.lendme.utils;

import java.util.Comparator;

import com.lendme.entities.User;

/**
 * This class provides an implementation for the compareTo method in
 * Comparator interface for User objects based on its score.
 * It can be seen as a strategy.
 */
public class UserRankingComparatorStrategy implements Comparator<User>{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
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
