package entities.util;

import java.util.Comparator;

import main.LendMe;
import entities.Item;
import entities.User;

public class ComparatorOfItems implements Comparator<Item>{

	@Override
	public int compare(Item o1, Item o2) {
		try {
			User user1 = LendMe.getItemOwner(o1.getID());
			User user2 = LendMe.getItemOwner(o2.getID());
			
			//Subtraction between user's reputation 
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return 0;
	}

}
