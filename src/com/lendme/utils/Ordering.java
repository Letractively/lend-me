package com.lendme.utils;

import java.util.List;

import com.lendme.entities.Item;
import com.lendme.entities.User;

public abstract class Ordering implements OrderedSearch{

	OrderedSearch search;
	
	public Ordering(OrderedSearch previous){
		search = previous;
	}
	
	public List<Item> doSearch(User searcher, String value) {
		List<Item> result = search.doSearch(searcher, value);
		return searchOrderVariant(result);
	}
	
	public abstract List<Item> searchOrderVariant(List<Item> result);

}
