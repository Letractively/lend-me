package com.lendme.client;

import java.io.Serializable;

public class ItemInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final String itemName;
	final String category;
	final String description;
	final String creationDate;
	final String itemId;
	final String lendingId;
	final String[] interested;
	final boolean belongsToMe;
	final ItemState state;

	public ItemInfo( String itemName, String category, String description, String creationDate, String itemId,
			String lendingId, String[] interested, boolean belongsToMe, ItemState state){
		this.itemName = itemName;
		this.category = category;
		this.description = description;
		this.creationDate = creationDate;
		this.itemId = itemId;
		this.lendingId = lendingId;
		this.interested = interested;
		this.belongsToMe = belongsToMe;
		this.state = state;
	}

	public String getItemName() {
		return itemName;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public String getItemId() {
		return itemId;
	}

	public String getLendingId() {
		return lendingId;
	}

	public String[] getInterested() {
		return interested;
	}

	public boolean belongsToMe() {
		return belongsToMe;
	}

	public ItemState getState() {
		return state;
	}

}