package com.lendme.utils;

public abstract class Ordering<Item> implements SearchAndOrdering<Item> {

	SearchAndOrdering<Item> search;
	
	public Ordering(SearchAndOrdering<Item> search){
		this.search = search;
	}
	
}
