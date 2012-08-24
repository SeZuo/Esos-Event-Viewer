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

package com.eSOS.eventViewer.client.view;


import java.util.HashMap;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.eSOS.eventViewer.client.model.EventModel;
import com.eSOS.eventViewer.client.model.Location;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;



public class EventView extends HorizontalPanel implements HasDragHandle {
	private EventModel model;
	private Label eventLabel = new Label("This is the event label");
	private Button editButton = new Button();
	public Image dragHandle = new Image("resources/images/mine/move.png");

	private static HashMap<Location,String> colorMap = new HashMap<Location,String>(Location.size());
	public enum ColorTheme {
		BLUE("blue"),
		GREEN("green"),
		VIOLET("violet"),
		LIME("lime");

		private final String styleDependentName;
		private ColorTheme(String styleDependentName) {
			this.styleDependentName = styleDependentName;
		}
		@Override
		public String toString() {
			return styleDependentName;
		}
	}
	static {
		int i = 0;
		while (Location.hasNext()) {
			if (i%4 == 0) {
				colorMap.put(Location.next(), ColorTheme.BLUE.toString());
			} else if (i%4 == 1) {
				colorMap.put(Location.next(), ColorTheme.GREEN.toString());
			} else if (i%4 == 2) {
				colorMap.put(Location.next(), ColorTheme.VIOLET.toString());
			} else if (i%4 == 3) {
				colorMap.put(Location.next(), ColorTheme.LIME.toString());
			}
			i++;
		}
	}

	public EventView(EventModel model) {
		this.model = model;

		add(dragHandle);
		dragHandle.setStyleName("moveImg");
		
		// wrap the eventLabel in a box
		
		
		add(eventLabel);
		eventLabel.setStyleName("eventLabel");

		add(new Label(""));
		add(editButton);

		editButton.setToolTip("Edit the event");
		editButton.setMenu(createEditMenu());

		updateVisualStyle();
		update();
	}

	public void updateVisualStyle() {
		removeStyleName("event-glossy-blue");
		removeStyleName("event-glossy-lime");
		removeStyleName("event-glossy-violet");
		removeStyleName("event-glossy-green");
		setStylePrimaryName("event");
		addStyleDependentName("glossy-"+colorMap.get(model.getLocation()));
	}

	private Menu createEditMenu() {  
		Menu menu = new Menu();  

		MenuItem changeRoomMenu = new MenuItem("Change room");
		menu.add(changeRoomMenu);  
		Menu changeRoomSub = new Menu();
		changeRoomSub.add(new MenuItem("Room1"));
		changeRoomSub.add(new MenuItem("Room2"));
		changeRoomSub.add(new MenuItem("Room3"));
		changeRoomSub.add(new MenuItem("Room4"));

		changeRoomMenu.setSubMenu(changeRoomSub);

		menu.add(new MenuItem("Edit"));    
		return menu;  
	}

	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);
		if(delayedEventResizeListener)
			addResizeListenerNow(eventResizeListener);
	}
	
	//TODO check if this is doing something -> right now, it's not
	@Override
	protected void onLoad() {
		super.onLoad();
//		this.setStyleAttribute("style", "z-index:11");
	}

	/**
	 * This variable stores the {@link EventResizeListener} until the object is rendered
	 */
	private EventResizeListener eventResizeListener = new EventResizeListener(null,null);
	private boolean delayedEventResizeListener = false;
	public void addListener(EventResizeListener listener) {
		eventResizeListener = listener;
		if(isRendered()) {
			addResizeListenerNow(eventResizeListener);
		} else {
			delayedEventResizeListener = true;
		}
	}

	private Resizable resizable;
	private void addResizeListenerNow(EventResizeListener listener) {
		resizable = new Resizable(this, "e w");
		//TODO this should change when the drop grid is changed
		resizable.setMinWidth(27);
		resizable.addResizeListener(listener);
	}

	private void updateTooltipContent () {
		this.setToolTip(model.getName());
	}


	public void update() {
		updateTooltipContent();
		eventLabel.setText(model.getName());
	}

	/**
	 * Create a new EventView object using the EventModel provided
	 * @param model
	 * @return EventView created from model
	 */
	public static EventView create(EventModel model) {
		return new EventView(model);
	}

	@Override
	public Widget getDragHandle() {
		return dragHandle;
	}

	@Deprecated
	public void setZindex(int index) {
		getElement().setAttribute("style",
				getElement().getAttribute("style")+";z.index:"+index);
	}

	@SuppressWarnings("unchecked")
	public EventModel getModel() {
		return model;
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		updateFeatureSet(width);
	}
	
	/**
	 * Do not use this method. Use setWidth(int width) instead.
	 */
	@Override
	public void setWidth(String width) {
		super.setWidth(width);
	}

	/**
	 * This will determine which features to set visible/on depending on the width
	 * For example, the resize listener is removed for events smaller than 25 pixs
	 * @param width
	 */
	public void updateFeatureSet(int width) {
		dragHandle.setVisible(true);
		eventLabel.setVisible(true);
		editButton.setVisible(true);
		if (width < 125) {
			editButton.setVisible(false);
			if (width < 100)
				eventLabel.setVisible(false);
				if (width < 25)
					dragHandle.setVisible(false);
					if(width < 10) {
						resizable.release();
					} else {
						addListener(eventResizeListener);
					}
		}
	}
}
