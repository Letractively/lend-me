package com.lendme.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class LeftOptionsSideBar extends Composite {
	public LeftOptionsSideBar() {
		
		AbsolutePanel rootPanel = new AbsolutePanel();
		initWidget(rootPanel);
		rootPanel.setSize("275px", "700px");
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		rootPanel.add(absolutePanel, 10, 10);
		absolutePanel.setSize("100px", "100px");
	}
}
