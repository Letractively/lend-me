package com.lendme.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lendme.entities.Item;
import com.lendme.entities.User;

/**
 * This class implements the OrderedSerach interface, 
 * providing a search strategy based on the id of the item.
 */
public class IDSearchStrategy implements OrderedSearch {

	/* (non-Javadoc)
	 * @see com.lendme.utils.OrderedSearch#doSearch(com.lendme.entities.User, java.lang.String)
	 */
	@Override
	public List<Item> doSearch(User searcher, String value) {

		List<Item> result = new ArrayList<Item>();
		Set<User> scope = searcher.getFriends();
		scope.add(searcher);
		for ( User friend : scope ){
			for ( Item item : friend.getAllItems() ){
				if ( item.getID().toUpperCase().contains(value.toUpperCase()) ){
					result.add(item);
				}
			}
		}
		return result;
	}

}
