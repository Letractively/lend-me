package com.lendme.utils;

import java.util.Collection;

import com.lendme.entities.User;

public class NameSearchStrategy<Item> implements Search<Item> {

	@Override
	public Collection<Item> getOrderedResult(User searcher, String value) {
		return null;
	}

}
