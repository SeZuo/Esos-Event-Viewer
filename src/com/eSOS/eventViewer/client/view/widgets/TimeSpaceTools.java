/*
 * Copyright 2010 Sebastien Zurfluh
 * 
 * This file is part of "ESOS Event Viewer".
 * 
 * "ESOS Event Viewer" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * "ESOS Event Viewer" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with "ESOS Event Viewer".  If not, see <http://www.gnu.org/licenses/>.
 */

package com.eSOS.eventViewer.client.view.widgets;

import com.eSOS.eventViewer.client.control.TimeSpacePresenter;
import com.eSOS.eventViewer.client.model.Location;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Create the buttons to add/remove lines	
 * @author Sebastien Zurfluh
 */
public class TimeSpaceTools extends VerticalPanel {	
	public TimeSpaceTools(final TimeSpacePresenter tsp, final Location location) {
		ImageButton plus = new ImageButton("resources/images/mine/plus-up.gif",
				"resources/images/mine/plus-over.gif",
				"resources/images/mine/plus-down.gif",
				"resources/images/mine/plus-disabled.gif");
		ImageButton minus = new ImageButton("resources/images/mine/minus-up.gif",
				"resources/images/mine/minus-over.gif",
				"resources/images/mine/minus-down.gif",
				"resources/images/mine/minus-disabled.gif");
		minus.disable();
		
		plus.setStyleName("sideButtons");
		plus.setTooltip("Add a visual line");
		plus.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//TODO change this with an event bus
				tsp.requestAddLine(location);
			}
		});
		minus.setStyleName("sideButtons");
		minus.setTooltip("Remove a visual line");
		

		add(minus);
		add(plus);
	}
	
	public void updatePosition(int positionFromTop) {
		this.getElement().setPropertyString("style", "position:relative;top:"+positionFromTop+"px");
	}
}
