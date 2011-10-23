package com.lendme;

import java.util.Set;

import com.lendme.entities.Item;

public class LendMeItemModule {
	
	/**
	 * Returns some item attribute.
	 * @param itemId the item id
	 * @param attribute the user attribute
	 * @return the item attribute value
	 * @throws Exception if parameters are invalid
	 */
	public  String getItemAttribute(String itemId, String attribute, String ownerSessionId,
			Profile viewerProfile) throws Exception{
		return viewerProfile.getItemAttribute(itemId, attribute);
	}
	
	/**
	 * Returns the items of the user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of items
	 * @throws Exception if user doesn't exists
	 */
	public  Set<Item> getItems(Profile viewerProfile) throws Exception {
		return viewerProfile.getOwnerItems();
	}
	
	
	

}
