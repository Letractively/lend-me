package com.lendme.utils;

import java.util.Collections;
import java.util.List;

import com.lendme.entities.Item;
import com.lendme.utils.Ordering;
import com.lendme.utils.OrderedSearch;

/**
 * This class extends the Ordering abstract class, 
 * providing a decreasing ordering strategy for a query.
 */
public class DecreasingOrderingStrategy extends Ordering {

	public DecreasingOrderingStrategy(OrderedSearch previous) {
		super(previous);
	}

	@Override
	public List<Item> searchOrderVariant(List<Item> result) {
		Collections.reverse(result);
		return result;
	}

}
