package com.lendme;

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
	

}
