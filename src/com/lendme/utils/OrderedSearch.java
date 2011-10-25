package com.lendme.utils;

import java.util.List;

import com.lendme.entities.Item;
import com.lendme.entities.User;

public interface OrderedSearch {

	public List<Item> doSearch(User searcher, String value);
	
}