package com.lendme.server.utils;

import java.util.Collections;
import java.util.List;

import com.lendme.server.entities.Item;

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
