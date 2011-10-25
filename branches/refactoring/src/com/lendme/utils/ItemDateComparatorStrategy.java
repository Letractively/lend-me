package com.lendme.utils;

import java.util.Comparator;

import com.lendme.entities.Item;

public class ItemDateComparatorStrategy implements Comparator<Item> {

	@Override
	public int compare(Item arg0, Item arg1) {
		return arg1.getCreationDate().compareTo(arg0.getCreationDate());
	}

}
