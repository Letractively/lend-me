package com.lendme.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lendme.server.entities.Item;
import com.lendme.server.entities.User;

/**
 * This class implements the OrderedSerach interface, 
 * providing a search strategy based on the name of the item.
 */
public class NameSearchStrategy implements OrderedSearch {

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
				if ( item.getName().toUpperCase().contains(value.toUpperCase()) ){
					result.add(item);
				}
			}
		}
		return result;
	}

}
