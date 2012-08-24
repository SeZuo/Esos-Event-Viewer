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

package com.eSOS.eventViewer.client.model;

import java.util.HashMap;
import java.util.LinkedList;

import org.gwttime.time.Duration;
import org.gwttime.time.Interval;

import com.eSOS.eventViewer.client.control.Config;
import com.extjs.gxt.ui.client.widget.Info;

public class TimeSpaceModel {
	public TimeSpaceModel() {
		while(Location.hasNext()) {
			setLocationHeight(Config.DEFAULT_LOCATIONS_HEIGHT,Location.next());
		}
	}
	
	/**
	 * Range (starting and ending dates) defining the view
	 */
	private Interval range = Config.INITIAL_TIME_RANGE;
	/**
	 * Time interval per cell in seconds
	 */
	private int timeIntervalMinutes = Config.TIMELINE_INITIAL_INTERVAL_MINUTES;
	/**
	 * Time interval per cell in pixels 
	 * A cell is the interval between two discrete consecutive time graduations (vertical)
	 */
	private int timeIntervalPixels = Config.TIMELINE_INITIAL_INTERVAL_PIXELS;
	
	private int boundaryWidth = Config.BOUNDARY_WIDTH;
	
	private int totalNumberOfLines = 0; // starts at 0 
	
	private int lineHeight = Config.TIMELINE_LINE_HEIGHT;
	
	//TODO I'm adding a deprecate anotation in order to see if this is used
	@Deprecated
	private int zoomFactor = 100;
	
	/**
	 * Number of line for a specific location
	 */
	private HashMap<Location, Integer> locationLineHeights = new HashMap<Location, Integer>();
	
	/**
	 * List events' data.
	 * Views are stored inside the TimeSpaceView
	 */
	private LinkedList<EventModel> eventModels = new LinkedList<EventModel>();

	
	

	//------------------------------------------------------------------------------------
	// Getters and setters

	
	
	public void setTimeIntervalPixels(int timeIntervalPixels) {
		this.timeIntervalPixels = timeIntervalPixels;
		// this should fire a TimeIntervalChangeEvent
	}
	public int getTimeIntervalPixels() {
		return timeIntervalPixels;
	}
	
	public void setTimeIntervalMinutes(int timeIntervalMinutes) {
		this.timeIntervalMinutes = timeIntervalMinutes;
	}

	public int getTimeIntervalMinutes() {
		return timeIntervalMinutes;
	}
	
	public int getTimeIntervalNumber() {
		return (int) (range.toDuration().getStandardMinutes()/getTimeIntervalMinutes());
	}
	
	/**
	 * Use this to update the event list
	 * @param events
	 */
	public void setEvents(LinkedList<EventModel> events) {
		this.eventModels = events;
		// Order the events by location: this is a great performance improvement
		bubbleSort(this.eventModels);
	}
	public LinkedList<EventModel> getEvents() {
		return eventModels;
	}
	public void setNumberOfLines(int numberOfLines) {
		this.totalNumberOfLines = numberOfLines;
	}
	public int getNumberOfLines() {
		return totalNumberOfLines;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}
	public int getLineHeight() {
		return lineHeight;
	}
	
	/**
	 * Changes the number of lines for a specific location
	 * @param numberOfLines
	 */
	public void setLocationHeight(int numberOfLines, Location location) {
		//TODO check if this changes the value for a given key or just adds it
		int addedLines = numberOfLines;
		if (locationLineHeights.containsKey(location))
			addedLines -= locationLineHeights.get(location);
		locationLineHeights.put(location, numberOfLines);
		// update the total number of lines
		setNumberOfLines(
				getNumberOfLines()
				+ addedLines);
		//TODO fire heightChangedEvent
	}
	public HashMap<Location,Integer> getLocationHeights () {
		return locationLineHeights;
	}
	
	/**
	 * This set the range of the view
	 * @param range
	 */
	public void setRange(Interval range) {
		this.range = range;
	}
	/**
	 * Gives the range of the view
	 * @return
	 */
	public Interval getRange() {
		Info.display("getRange call","","");
		return range;
	}
	
	public int getBoundaryHeight() {
		return getNumberOfLines()*getLineHeight();
	}
	
	/**
	 * Gets the boundary width
	 * careful, for the inner width of the draggable area use
	 * getDraggableAreaWidth() instead
	 * @return int, the boundary width
	 */
	public int getBoundaryWidth() {
		return boundaryWidth;
	}
	
	/**
	 * Gets the inner width of the draggable area
	 * Obviously, getTimeIntervalNumber() * timeIntervalPixels gives
	 * the total size of the horizontal ruler
	 * @return int, the inner width of the draggable area
	 */
	public int getDraggableAreaWidth() {
		return getTimeIntervalNumber() * getTimeIntervalPixels();
	}
	@Deprecated
	public int getTimeTotalSize() {
		return getTimeIntervalNumber() * getTimeIntervalPixels();
	}
	
	//------------------------------------------------------------------------------------
	
	
	
	/**
	 * Order an ArrayList<EventModel> by Location
	 * using bubblesort algorithm
	 * @param x
	 * @return x (ordered)
	 */
	public static void bubbleSort(LinkedList<EventModel> x) {
	    int xSize = x.size();
		int newLowest = 0;            // index of first comparison
	    int newHighest = xSize-1;  // index of last comparison
	    while (newLowest < newHighest) {
	        int highest = newHighest;
	        int lowest  = newLowest;
	        newLowest = xSize;    // start higher than any legal index
	        for (int i=lowest; i<highest; i++) {
	            if (x.get(i).getLocation().compareTo(x.get(i+1).getLocation()) == 1) {
	               // exchange elements
	               EventModel temp = x.get(i);  x.set(i, x.get(i+1));  x.set(i+1, temp);
	               if (i<newLowest) {
	                   newLowest = i-1;
	                   if (newLowest < 0) {
	                       newLowest = 0;
	                   }
	               } else if (i>newHighest) {
	                   newHighest = i+1;
	               }
	            }
	        }
	    }
	}

	
	
	
}
