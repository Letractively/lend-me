package com.lendme.utils;

import java.util.Comparator;

import com.lendme.entities.User;

public class ComparatorOfDateStrategy implements Comparator<User>{

	@Override
	public int compare(User arg0, User arg1) {
		return  - arg0.getCreationDate().compareTo(arg1.getCreationDate());
	}

}
