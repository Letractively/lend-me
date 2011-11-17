package com.lendme.server.utils;

import java.util.Collections;
import java.util.List;

import com.lendme.server.entities.Item;


/**
 * This class extends the Ordering abstract class, 
 * providing an ordering strategy for a query based
 * on the reputation of the item owner.
 */
public class ReputationOrderingStrategy extends Ordering {

	public ReputationOrderingStrategy(OrderedSearch previous) {
		super(previous);
	}

	/* (non-Javadoc)
	 * @see com.lendme.utils.Ordering#searchOrderVariant(java.util.List)
	 */
	@Override
	public List<Item> searchOrderVariant(List<Item> result) {
		Collections.sort(result, new ItemOwnerRankingComparatorStrategy());
		return result;
	}

}
