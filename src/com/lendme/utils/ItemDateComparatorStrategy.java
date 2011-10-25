package com.lendme.utils;

import java.util.Comparator;

import com.lendme.entities.Item;

/**
 * This class provides an implementation for the compareTo method in
 * Comparator interface for User objects based in the creation date of the item.
 * It can be seen as a strategy.
 */
public class ItemDateComparatorStrategy implements Comparator<Item> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Item arg0, Item arg1) {
		return arg1.getCreationDate().compareTo(arg0.getCreationDate());
	}

}
