package com.lendme.utils;

import java.util.Collections;
import java.util.List;

import com.lendme.entities.Item;

public class CreationDateOrderingStrategy extends Ordering {

	public CreationDateOrderingStrategy(OrderedSearch previous) {
		super(previous);
	}

	@Override
	public List<Item> searchOrderVariant(List<Item> result) {
		Collections.sort(result, new ItemDateComparatorStrategy());
		return result;
	}

}
