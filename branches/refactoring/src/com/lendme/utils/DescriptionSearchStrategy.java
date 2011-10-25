package com.lendme.utils;

import java.util.ArrayList;
import java.util.List;

import com.lendme.entities.Item;
import com.lendme.entities.User;

public class DescriptionSearchStrategy implements OrderedSearch {

	@Override
	public List<Item> doSearch(User searcher, String value) {

		List<Item> result = new ArrayList<Item>();
		for ( User friend : searcher.getFriends() ){
			for ( Item item : friend.getAllItems() ){
				if ( item.getDescription().toUpperCase().contains(value.toUpperCase()) ){
					result.add(item);
				}
			}
		}
		return result;
	}

}
