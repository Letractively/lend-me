package com.lendme.utils;

import java.util.List;

import com.lendme.entities.Item;
import com.lendme.entities.User;

/**
 * This interface provides the signature of a method
 * which will be implemented by classes that provide
 * different strategies to search for an item in the system
 */
public interface OrderedSearch {

	/**
	 * This method performs a query in the items existent
	 * in the system selecting the item by the presence
	 * of a value in the its properties. 
	 * @param searcher the user who requested the search
	 * @param value the value for which the query will look for
	 * @return the list of items selected
	 */
	public List<Item> doSearch(User searcher, String value);
}