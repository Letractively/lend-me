package com.lendme.utils;

import java.util.Collection;

import com.lendme.entities.User;

public class CreationDateOrderingStrategy<Item> extends Ordering<Item> {

	public CreationDateOrderingStrategy(SearchAndOrdering<Item> search) {
		super(search);
	}

	@Override
	public Collection<Item> getOrderedResult(User searcher, String value) {
		return null;
	}

}
