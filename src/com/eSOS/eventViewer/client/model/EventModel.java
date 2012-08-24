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

import org.gwttime.time.DateTime;

public class EventModel {
	/**
	 * Location ie room
	 */
	private Location location;
	
	/**
	 * Name of the event
	 */
	private String name;
	
	/**
	 * Comment about the event
	 */
	private String comment;
	
	/**
	 * Beginning of the event
	 */
	private DateTime startDateTime;
	
	/**
	 * End of the event
	 */
	private DateTime endDateTime;
	
	/**
	 * Event Model constructor
	 * @param eventName
	 * @param startDateTime
	 * @param endDateTime
	 * @param location
	 */
	public EventModel(String eventName, String comment, DateTime startDateTime,
			DateTime endDateTime, Location location) {
		this.setName(eventName);
		this.setComment(comment);
		this.setLocation(location);
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.location = location;
	}
	
	public void setEndDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
	public DateTime getEndDateTime() {
		return endDateTime;
	}
	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}
	
	private String servId = "";
	/**
	 * This gives the id associated with this event on the server
	 * @return the id associated with this event on the server
	 */
	public String getServId() {
		return servId;
	}
	/**
	 * The servId can only be set once.
	 * @param servId
	 */
	public void setServId(String servId) {
		assert (this.servId == "");
		this.servId = servId;
	}
}
