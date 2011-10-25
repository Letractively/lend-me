package com.lendme.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lendme.entities.Item;
import com.lendme.entities.User;

public class CategorySearchStrategy implements OrderedSearch {

	@Override
	public List<Item> doSearch(User searcher, String value) {

		List<Item> result = new ArrayList<Item>();
		Set<User> scope = searcher.getFriends();
		scope.add(searcher);
		for ( User friend : scope ){
			for ( Item item : friend.getAllItems() ){
				if ( item.getCategory().toUpperCase().contains(value.toUpperCase()) ){
					result.add(item);
				}
			}
		}
		return result;
	}

}
