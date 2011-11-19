package com.lendme.server.utils;

import java.util.List;

import com.lendme.server.entities.Item;

/**
 * This class extends the Ordering abstract class, 
 * providing an increasing ordering strategy for a query.
 */
public class IncreasingOrderingStrategy extends Ordering {

	public IncreasingOrderingStrategy(OrderedSearch previous) {
		super(previous);
	}

	/* (non-Javadoc)
	 * @see com.lendme.utils.Ordering#searchOrderVariant(java.util.List)
	 */
	@Override
	public List<Item> searchOrderVariant(List<Item> result) {
		return result;
	}


}
