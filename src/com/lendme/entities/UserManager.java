package com.lendme.entities;

import java.util.HashSet;
import java.util.Set;

public class UserManager {
	
	private Set<User> myFriends = new HashSet<User>();

	public boolean isMyFriend(User user) {
		return myFriends.contains(user);
	}
}
