package com.lendme.utils;

import java.util.Collection;

import com.lendme.entities.User;

public class IncreasingOrderingStrategy<Item> extends Ordering<Item> {

	public IncreasingOrderingStrategy(SearchAndOrdering<Item> search) {
		super(search);
	}

	@Override
	public Collection<Item> getOrderedResult(User searcher, String value) {
		return null;
	}

}
