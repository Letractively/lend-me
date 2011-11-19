package com.lendme.server.utils;

import java.util.List;

import com.lendme.server.entities.Item;
import com.lendme.server.entities.User;

/**
 * This class implements the ordered search, providing
 * an implementation for the search which delegates the
 * search for the previous searcher and then then delegates
 * the ordering for the subclasses, which have to implement it.
 */
public abstract class Ordering implements OrderedSearch{

	OrderedSearch search;
	
	public Ordering(OrderedSearch previous){
		search = previous;
	}
	
	/* (non-Javadoc)
	 * @see com.lendme.utils.OrderedSearch#doSearch(com.lendme.entities.User, java.lang.String)
	 */
	public List<Item> doSearch(User searcher, String value) {
		List<Item> result = search.doSearch(searcher, value);
		return searchOrderVariant(result);
	}
	
	/**
	 * This method returns an ordered list of the given search
	 * result based on a parameter specified in the implementation
	 * @param result the result of the previous search (or ordenation)
	 * @return the ordered list with the search result
	 */
	public abstract List<Item> searchOrderVariant(List<Item> result);

}
