package com.lendme.utils;

import java.util.List;

import com.lendme.entities.Item;
import com.lendme.utils.Ordering;
import com.lendme.utils.OrderedSearch;

public class IncreasingOrderingStrategy extends Ordering {

	public IncreasingOrderingStrategy(OrderedSearch previous) {
		super(previous);
	}

	@Override
	public List<Item> searchOrderVariant(List<Item> result) {
		return result;
	}


}
