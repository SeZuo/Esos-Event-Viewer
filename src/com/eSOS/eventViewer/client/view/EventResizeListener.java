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

import java.util.ArrayList;

import com.eSOS.eventViewer.client.control.dynamics.HasPositioner;
import com.eSOS.eventViewer.client.control.dynamics.WidgetPositioner;
import com.eSOS.eventViewer.client.model.TimeSpaceModel;
import com.extjs.gxt.ui.client.event.ResizeEvent;
import com.extjs.gxt.ui.client.event.ResizeListener;

public class EventResizeListener extends ResizeListener implements HasPositioner {
	private int offsetWidthAtStart = 0;
	private int absoluteLeftAtStart = 0;
	private ArrayList<EventView> eventViews;
	private TimeSpaceModel timeSpaceModel;
	
	/**
	 * Constructor
	 * @param eventViews
	 */
	public EventResizeListener(ArrayList<EventView> eventViews, TimeSpaceModel timeSpaceModel) {
		this.eventViews = eventViews;
		this.timeSpaceModel = timeSpaceModel;
	}
	
	@Override
	public void resizeStart(ResizeEvent re) {
		offsetWidthAtStart = re.getComponent().getOffsetWidth();
		absoluteLeftAtStart = re.getComponent().getAbsoluteLeft();
	}

	@Override
	public void resizeEnd(ResizeEvent re) {
		EventView sourceEvent = ((EventView)re.getComponent());
		
		int difference = (sourceEvent.getOffsetWidth()-offsetWidthAtStart);
		
		// keep to the grid
		int width = Math.round((float) sourceEvent.getOffsetWidth() / timeSpaceModel.getTimeIntervalPixels())
		* timeSpaceModel.getTimeIntervalPixels();		
		
		sourceEvent.setWidth(width);
		
		// the next part correct a strange behaviour when resizing by the left side
		//TODO (check if this is a Firefox 3.6 only error)
		int gridDifference = Math.round((float) difference / timeSpaceModel.getTimeIntervalPixels())
		* timeSpaceModel.getTimeIntervalPixels();
		if (absoluteLeftAtStart != sourceEvent.getAbsoluteLeft()) {
			// the event has been resized by the left
			sourceEvent.el().setX(absoluteLeftAtStart-gridDifference);
		}
		// end of part
		
		if (hasPositioner() && difference > 0) { // the element is bigger than before
			positioner.resolveOverlapping(sourceEvent,eventViews);
		}
		
		
		
		com.extjs.gxt.ui.client.widget.Info.display("Resize element", "value ="
				+  difference);
	}

	
	private WidgetPositioner<EventView> positioner =
		new WidgetPositioner<EventView>(null,null);
	/**
	 * This boolean permits to check if the WidgetPositionner is registered
	 * (debug purpose)
	 */
	private boolean hasPositioner = false;
	
	public void registerPositioner(WidgetPositioner<EventView> positioner) {
		this.positioner = positioner;
		// this is used by asserts (debug purpose)
		this.hasPositioner = true;
	}

	public boolean hasPositioner() {
		return hasPositioner;
	}
	
}