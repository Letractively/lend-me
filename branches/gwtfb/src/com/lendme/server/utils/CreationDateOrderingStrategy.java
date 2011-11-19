package com.lendme.server.utils;

import java.util.Collections;
import java.util.List;

import com.lendme.server.entities.Item;

/**
 * This class extends the Ordering abstract class, 
 * providing an ordering strategy for a query based
 * on the creation date of the item.
 */
public class CreationDateOrderingStrategy extends Ordering {

	public CreationDateOrderingStrategy(OrderedSearch previous) {
		super(previous);
	}

	/* (non-Javadoc)
	 * @see com.lendme.utils.Ordering#searchOrderVariant(java.util.List)
	 */
	@Override
	public List<Item> searchOrderVariant(List<Item> result) {
		Collections.sort(result, new ItemDateComparatorStrategy());
		return result;
	}

}
