package com.lendme.utils;

import java.util.Collections;
import java.util.List;

import com.lendme.entities.Item;
import com.lendme.utils.ItemOwnerRankingComparatorStrategy;
import com.lendme.utils.Ordering;
import com.lendme.utils.OrderedSearch;

public class ReputationOrderingStrategy extends Ordering {

	public ReputationOrderingStrategy(OrderedSearch previous) {
		super(previous);
	}

	@Override
	public List<Item> searchOrderVariant(List<Item> result) {
		Collections.sort(result, new ItemOwnerRankingComparatorStrategy());
		return result;
	}

}
