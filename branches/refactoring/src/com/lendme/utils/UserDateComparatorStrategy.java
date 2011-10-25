package com.lendme.utils;

import java.util.Comparator;

import com.lendme.entities.User;

public class UserDateComparatorStrategy implements Comparator<User> {

	@Override
	public int compare(User arg0, User arg1) {
		return arg1.getCreationDate().compareTo(arg0.getCreationDate());
	}

}
