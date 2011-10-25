package com.lendme.utils;

import java.util.Comparator;

import com.lendme.LendMeFacade;
import com.lendme.entities.User;
import com.lendme.entities.Item;

/**
 * This class provides an implementation for the compareTo method in
 * Comparator interface for Item objects based on the score of the
 * item owner. It can be seen as a strategy.
 */
public class ItemOwnerRankingComparatorStrategy implements Comparator<Item>{
	
	private LendMeFacade lendMe = new LendMeFacade();

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Item o1, Item o2) {
		try {
			
			User user1 = lendMe.getItemOwner(o1.getID());
			User user2 = lendMe.getItemOwner(o2.getID());
			
			//Subtraction between user's reputation
			return -(user2.getScore() - user1.getScore());
			
		} catch (Exception e) {
			return 0;
		}
		
	}

}
