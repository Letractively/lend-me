package com.lendme.utils;

import java.util.Comparator;

import com.lendme.LendMe;
import com.lendme.entities.User;
import com.lendme.entities.Item;

public class ComparatorOfItemsStrategy implements Comparator<Item>{

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
