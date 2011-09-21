package com.lendme;

import java.util.Comparator;

public class ComparatorOfItems implements Comparator<Item>{

	@Override
	public int compare(Item o1, Item o2) {
		try {
			
			User user1 = LendMe.getItemOwner(o1.getID());
			User user2 = LendMe.getItemOwner(o2.getID());
			
			//Subtraction between user's reputation
			return -(user2.getScore() - user1.getScore());
			
		} catch (Exception e) {
			return 0;
		}
		
	}

}
