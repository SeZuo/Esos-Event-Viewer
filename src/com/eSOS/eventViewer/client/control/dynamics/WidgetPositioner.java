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

package com.eSOS.eventViewer.client.control.dynamics;

import java.util.HashMap;

import org.gwttime.time.DateTime;
import org.gwttime.time.Duration;

import com.eSOS.eventViewer.client.control.TimeSpacePresenter;
import com.eSOS.eventViewer.client.control.dynamics.AbsolutePositionDropController.Draggable;
import com.eSOS.eventViewer.client.model.Location;
import com.eSOS.eventViewer.client.model.TimeSpaceModel;
import com.eSOS.eventViewer.client.view.EventView;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class permits to control overlapping when W is resized or when it's
 * dropped somewhere
 * 
 * @author Sebastien Zurfluh
 *
 * @param <W> in general W is an EventView but there will be several different EventView to
 * display (small, detailed, zone...)
 */
public class  WidgetPositioner <W extends Widget> {
	//TODO change this with a EventBus
	private TimeSpacePresenter timeSpacePresenter;
	private TimeSpaceModel timeSpaceModel;

	/**
	 * This variable gives the difference between the given relative value for the desired
	 * position and the actual value in an absolute referential
	 * It must be set with the setRelativeReferential(int x, int y)
	 */
	private int topRelativeDifference = 0;
	private int leftRelativeDifference = 0;


	public WidgetPositioner(TimeSpacePresenter timeSpacePresenter,
			TimeSpaceModel timeSpaceModel) {
		this.timeSpacePresenter = timeSpacePresenter;
		this.timeSpaceModel = timeSpaceModel;
	}
	/**
	 * This function test if the event is overlapping another one.
	 * Note: desired positions need to be exact absolute positions.
	 * @param draggedEvent, the event which just been dragged
	 * or needs to be checked for overlapping
	 * @param desiredLeftPosition, as said. Absolute position required.
	 * @param desiredTopPosition, as said. Absolute position required.
	 * @param existingEvents, list existing events or whatever could be
	 * overlapping.
	 * @return boolean, true if the event is overlapping an existing one
	 */
	public boolean isOverlapping (W suspectWidget,
			int desiredLeftPosition,
			int desiredTopPosition,
			Iterable<W> existingWList) {		
		
		for (W otherW : existingWList) {
			if ( otherW.equals(suspectWidget) )
				continue;				

			// 'if' checks if the desired position is overlapping the existing event

			// are they on the same line?
			if ( desiredTopPosition == otherW.getAbsoluteTop() ) {
				// if the starting of the suspect is inside the other one
				if ( desiredLeftPosition >= otherW.getAbsoluteLeft() ) {
					if (desiredLeftPosition < 
						otherW.getAbsoluteLeft() + otherW.getElement().getOffsetWidth() )
						return true;
				// if start is before and the ending of the suspect is inside or after
				} else if ( desiredLeftPosition+suspectWidget.getElement().getOffsetWidth() > otherW.getAbsoluteLeft()) {
					return true;
				}
						
			}
					
		}
		return false;
	}

	/**
	 * Resolves overlapping in case of drag and drop
	 * Note: for now this is only working with W.class==EventView.class
	 * @param dropTarget 
	 * @param suspectEvent, this {@link Draggable} must contain a widget of class W
	 * @param locationsHeight
	 */
	@SuppressWarnings("unchecked") //found no way to check that because W.class gives an error
	public void resolveDropOverlapping(Draggable suspectW) {
		assert (suspectW.widget.getClass().equals(EventView.class));
		
		//TODO change the next line
		Iterable<W> existingW = (Iterable<W>) timeSpacePresenter.getTimeSpaceView().getEventViews();
		
		// Convert relative to absolute referential
		int absoluteLeft = suspectW.desiredX+leftRelativeDifference;
		int absoluteTop = suspectW.desiredY+topRelativeDifference;

		boolean firstLoop = true;
		
		// Find desired location (same or new?) (desiredY and location heights are relative)
		Location desiredLocation = findLocationByTopPosition(suspectW.desiredY);
		
		// Thanks to this info we may fire a changeOfLocationRequest to the controller
		if (!((EventView)suspectW.widget).getModel().getLocation().equals(desiredLocation)) {
			com.extjs.gxt.ui.client.widget.Info.display("Location change detected",
					"Found desired location is "+desiredLocation.toString());
			timeSpacePresenter.requestChangeLocation((EventView)suspectW.widget,desiredLocation);

		}
		
		HashMap<Location, Integer> locationHeights =
			timeSpaceModel.getLocationHeights();
		int lineHeight = timeSpaceModel.getLineHeight();


		// Get the top and bottom relative position of the desired location
		int[] bounds = findLocationBounds(desiredLocation,
				locationHeights,
				lineHeight);
		int topLinePosition = bounds[0];
		int bottomLinePosition = bounds[1];
		
		assert (bottomLinePosition != 0);
		
		while (isOverlapping((W) suspectW.widget,
				absoluteLeft,
				absoluteTop,
				existingW)) {
			// this permits to add the widget in lines above in case of overlapping
			if (firstLoop) {
				suspectW.desiredY = topLinePosition;
			} else {
				// try putting the event in the next line (desiredY is a relative position)
				suspectW.desiredY += lineHeight;
				if (suspectW.desiredY >= bottomLinePosition) {
					//the event doesn't fit in the right location
					timeSpacePresenter.requestAddLine(desiredLocation);
					// now it does
				}
			}
			
			
			// do not forget to change these values for the check
			// (otherwise there's a stupid infinite loop error :>)
			absoluteTop = suspectW.desiredY+topRelativeDifference;
			firstLoop = false;
		}
	}
	
	/**
	 * Finds in which location the element is in. 
	 * @param top, relative top position of the element
	 * @return
	 */
	public Location findLocationByTopPosition (int top) {
		Location desiredLocation = Location.getInstance();
		while (top>=0 && Location.hasNext()) {
			desiredLocation = Location.next();
			top -= timeSpaceModel.getLocationHeights().get(desiredLocation) * timeSpaceModel.getLineHeight();
		}
		// ensure the location loop is completed
		Location.terminateIteration();
		//		com.extjs.gxt.ui.client.widget.Info.display("resolveDropOverlapping",
		//		"Found desired location is "+desiredLocation.toString());
		assert (!desiredLocation.equals(Location.getInstance()));
		
		return desiredLocation;
	}
	/**
	 * Get the top and bottom relative position of the desired location
	 * @param desiredLocation
	 * @return int[2], int[0] is the top and int[1] the bottom
	 */
	public static int[] findLocationBounds(Location desiredLocation,
			HashMap<Location, Integer> locationHeights,
			int lineHeight) {
		int topLinePosition = 0;
		int bottomLinePosition = 0;
		while(Location.hasNext()) {
			Location loc = Location.next();
			if (loc.equals(desiredLocation)) {
				bottomLinePosition =
					topLinePosition + locationHeights.get(loc) * lineHeight;
				break;
			} else {
				topLinePosition += locationHeights.get(loc) * lineHeight;
			}
		}
		Location.terminateIteration();
		return new int[] {topLinePosition, bottomLinePosition};
	}
	
	/**
	 * Resolves overlapping in the other cases (where there was no drop event)
	 * Note: this is nearly the same as resolveDropOverlapping but there is no way to merge them
	 * @param suspectW, has to be an event for now
	 * @param locationsHeight
	 */
	public void resolveOverlapping(W suspectW, Iterable<W> existingW) {
		boolean firstLoop = true;
		
		// Convert absolute to the relative referential
		// note: don't do this inside the while as several while loops might move the event quite a bit!
		int relativeLeft = suspectW.getAbsoluteLeft()-leftRelativeDifference;
		int relativeTop = suspectW.getAbsoluteTop()-topRelativeDifference;
		
		// Find desired location (same or new?) (desiredY and location heights are relative)
		Location desiredLocation = ((EventView) suspectW).getModel().getLocation();
		
		HashMap<Location, Integer> locationHeights =
			timeSpaceModel.getLocationHeights();
		int lineHeight = timeSpaceModel.getLineHeight();
		
		// Get the top and bottom relative position of the desired location
		int[] bounds = findLocationBounds(desiredLocation,
				locationHeights,
				lineHeight);
		int topLinePosition = bounds[0];
		int bottomLinePosition = bounds[1];
		
		
		while (isOverlapping(suspectW,
				suspectW.getAbsoluteLeft(),
				suspectW.getAbsoluteTop(),
				existingW)) {
			// this permits to add the widget in lines above in case of overlapping
			if (firstLoop) {
				relativeTop = topLinePosition;
			} else {
				// try putting the event in the next line (desiredY is a relative position)
				relativeTop += lineHeight;
				if (relativeTop >= bottomLinePosition) {
					//the event doesn't fit in the right location
					timeSpacePresenter.requestAddLine(desiredLocation);
					// now it does
				}
			}
			firstLoop = false;
			timeSpacePresenter.getTimeSpaceView().moveEvent((EventView)suspectW,
					relativeLeft,
					relativeTop);
		}
	}

	/**
	 * Gives the absolute position of the target panel in order to find relative positions
	 * Note: for positioning the event I had to consider the EventView 1px border also.   
	 * @param leftRelativeDifference
	 * @param topRelativeDifference
	 */
	public void setRelativeReferential(int leftRelativeDifference, int topRelativeDifference) {
		this.leftRelativeDifference = leftRelativeDifference;
		this.topRelativeDifference = topRelativeDifference;
	}
	
	/**
	 * Gives the left relative position of a Widget depending on it's start date 
	 */
	//TODO probably the most awful function ever written, rewrite
	public int getLeftPosition(EventView event) {
		DateTime start = event.getModel().getStartDateTime();
		Duration distanceFromViewStart = new Duration(
				timeSpaceModel.getRange().getStartMillis(),
				start.getMillis());
		int distance = (int) distanceFromViewStart.getStandardMinutes();
		int pixLeftPos = distance * timeSpaceModel.getTimeIntervalPixels() /timeSpaceModel.getTimeIntervalMinutes();
		return pixLeftPos;
	}
	public int getEventWidth(EventView eventView) {
		int minuteWidth = (int) new Duration(
				eventView.getModel().getStartDateTime(),
				eventView.getModel().getEndDateTime()).getStandardMinutes();
		int pixelWidth =
			minuteWidth * timeSpaceModel.getTimeIntervalPixels() /timeSpaceModel.getTimeIntervalMinutes();
		if (pixelWidth==0)
			return 1;
		return pixelWidth;
	}
}
