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

package com.eSOS.eventViewer.client.control;

import org.gwttime.time.DateTime;
import org.gwttime.time.Interval;

import com.eSOS.eventViewer.client.control.dynamics.WidgetPositioner;
import com.eSOS.eventViewer.client.model.Location;
import com.eSOS.eventViewer.client.model.TimeSpaceModel;
import com.eSOS.eventViewer.client.view.EventView;
import com.eSOS.eventViewer.client.view.TimeSpaceView;
import com.extjs.gxt.ui.client.widget.Info;

public class TimeSpacePresenter {
	TimeSpaceModel model;
	TimeSpaceView view;

	public TimeSpacePresenter(TimeSpaceModel model, TimeSpaceView view) {
		this.model = model;
		this.view = view;
	}
	
	public void usePositioner(boolean b) {
		if (b) {
			WidgetPositioner<EventView> positioner = new WidgetPositioner<EventView>(this, model);
			positioner.setRelativeReferential(view.getEventRelativeLeftDifference(),
					view.getEventRelativeTopDifference());
			view.registerPositioner(positioner);
		} else {
			view.removePositioner();
		}
	}
	
	public void requestAddLine(Location location) {
		requestAddLine(1, location);
	}
	
	public void requestAddLine(int step, Location loc) {
		if (model==null)
			Info.display("Message","MODEL NULL \n PRESENTER >> "+this.hashCode());
		else
			Info.display("Message","MODEL OK \n PRESENTER >> "+this.hashCode());
		model.setLocationHeight(
				model.getLocationHeights().get(loc)+step, loc);
		view.updateLocationGridDimensions();
//		view.updateTimeGridDimensions();
		view.updateBoundaryDimensions();
		view.pushEventsFromLocation(loc, step);
	}
	
	public TimeSpaceView getTimeSpaceView() {
		return view;
	}

	public void requestChangeLocation(EventView eventView,
			Location newLocation) {
		eventView.getModel().setLocation(newLocation);
		// i know i should create a model listener but...
		eventView.updateVisualStyle();
	}

	public void requestChangeTime(DateTime dateTime) {
		Info.display("Message","requestChangeTime called "+
				dateTime.toString());
		if (model==null)
			Info.display("Message","MODEL NULL \n PRESENTER >> "+this.hashCode());
		model.setRange(new Interval(dateTime,model.getRange().toDuration()));
		Info.display("Message","NEW RANGE= "+
				model.getRange().toString());
		model.setEvents(Synchroniser.getEventsInRange(model.getRange()));
		view.updateTimeEvents();
	}
	
	
	
}
