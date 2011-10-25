package com.lendme.utils;

import java.util.Comparator;

import com.lendme.entities.User;


/**
 * This class provides an implementation for the compareTo method in
 * Comparator interface for User objects based on the user's creation date.
 * It can be seen as a strategy.
 */
public class UserDateComparatorStrategy implements Comparator<User> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(User arg0, User arg1) {
		return arg1.getCreationDate().compareTo(arg0.getCreationDate());
	}

}
