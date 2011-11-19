package com.lendme.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ScrollPanel;

public class ItemsViewWidget extends Composite{
	
	ScrollPanel list = new ScrollPanel();
	
	
	public ItemsViewWidget(LendMeAsync lendMeAsync, String solicitorSession){
		
		lendMeAsync.getItems(solicitorSession, new AsyncCallback<String[]>() {
			
			@Override
			public void onSuccess(String[] result) {
				for ( int i=1; i<=result.length; i++ ){
					list.add(new Hyperlink("Item no." + i + " \n" + result[i],""));
				}
				initWidget(list);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				list.add(new Hyperlink("Could not retrieve information from the server.",""));
				initWidget(list);
			}
		});
	}
}

