package com.lendme.utils;

import java.util.Collection;

import com.lendme.entities.User;

public interface SearchAndOrdering<Item> {

	public Collection<Item> getOrderedResult(User searcher, String value);
	
}
