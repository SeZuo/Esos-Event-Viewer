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
 */package com.eSOS.eventViewer.client.jUnit;

import java.util.ArrayList;
import java.util.LinkedList;

import org.gwttime.time.DateTime;
import org.gwttime.time.Duration;
import org.gwttime.time.Interval;

import com.eSOS.eventViewer.client.model.EventModel;
import com.eSOS.eventViewer.client.model.Location;
import com.eSOS.eventViewer.client.view.EventView;
//import com.google.gwt.json.client.JSONParser;
//import com.google.gwt.json.client.JSONValue;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Random;

public class EventFactory {
	public static EventView createRandomEventView() {
		return new EventView(createRandomEvent());
	}
	public static EventModel createRandomEvent() {
		EventModel e = new EventModel(
				"Random event" + Random.nextInt(200),
				"Just a random event",
				new DateTime(), 
				new DateTime(),
				Location.next());
		Location.terminateIteration();
		return e;
	}
	public static EventModel createRandomEventInLocation(Location loc) {
		EventModel e = new EventModel(
				"Random event",
				"Just a random event",
				new DateTime(), 
				new DateTime(),
				loc);
		return e;
	}
	public static ArrayList<EventModel> createForAllLocations(int howMany) {
		ArrayList<EventModel> events = new ArrayList<EventModel>();
		int count = 0;
		for (int i = 0; i < howMany; i++) {
			while (Location.hasNext()) {
				events.add(
						new EventModel(
								"event"+count++,
								"Just a random event",
								new DateTime(), 
								new DateTime(),
								Location.next()));
			}
		}
		return events;
	}
	
	
	public static ArrayList<EventModel> createRandom(int howMany) {
		ArrayList<EventModel> events = new ArrayList<EventModel>();
		for (int i = 0; i < howMany; i++) {
			EventModel e =
				createRandomEventInLocation(Location.get(Random.nextInt(Location.size())));
			events.add(e);
		}
		
		return events;
	}
	
	public static ArrayList<EventModel> createRandomInTwoLocations(int howMany) {
		ArrayList<EventModel> events = new ArrayList<EventModel>();
		Location one = Location.next();
		for (int i = 0; i < howMany; i++) {
			EventModel e = createRandomEventInLocation(one);
			events.add(e);
		}
		Location.next();
		Location.next();
		Location two = Location.next();
		Location.terminateIteration();
		for (int i = 0; i < howMany; i++) {
			EventModel e = createRandomEventInLocation(two);
			events.add(e);
		}
		return events;
	}
	
	public static ArrayList<EventModel> createRandomInAllLocations(int howMany) {
		ArrayList<EventModel> events = new ArrayList<EventModel>();
		while (Location.hasNext()) {
			Location loc = Location.next();
			for (int i = 0; i < howMany; i++) {
				EventModel e = createRandomEventInLocation(loc);
				events.add(e);
			}
		}
		return events;
	}
	public static LinkedList<EventModel> createRandomInTimeRange(int howMany, Interval range) {
		assert range.toDurationMillis() <= Integer.MAX_VALUE;
		
		LinkedList<EventModel> events = new LinkedList<EventModel>();
		long rangeWidth = range.toDurationMillis();
		
		for (int i = 0; i < howMany; i++) {
			//Random size
			long begin = Random.nextInt((int) rangeWidth);
			long end = Random.nextInt((int) Duration.standardHours(6).getMillis());
			begin+=range.getStartMillis();
			end+=begin;
			//Random location
			Location loc = Location.get(Random.nextInt(Location.size()));
			EventModel e = createRandomEventInLocation(loc);
			e.setStartDateTime(new DateTime(begin));
			e.setEndDateTime(new DateTime(end));
			events.add(e);
		}
		return events;
	}
	
	/**
	 * Use this to transform the stupid malformed data from sven's model to a proper EventModel
	 * @param resId
	 * @param machId
	 * @param name
	 * @param comment
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @return a proper EventModel object
	 */
	public static EventModel createEventModel(String resId,
			String machId, String name, String comment,
			Long startDate, Long startTime,
			Long endDate, Long endTime) {
		DateTime startDateTime = new DateTime(startDate*1000).plus(startTime);
		DateTime endDateTime = new DateTime(endDate*1000).plus(endTime);
		EventModel eventModel = new EventModel(name, comment,
				startDateTime, endDateTime, Location.get(machId));
		eventModel.setServId(resId);
		return eventModel;
	}
	
//	public static ArrayList<EventModel> createWithData(JSONValue data) {
//		ArrayList<EventModel> events = new ArrayList<EventModel>();
//		
//		if(data.isNull() != null) {
//			return new ArrayList<EventModel>();
//		} else if (data.isArray() != null) {
//			
//		} else if (data.isBoolean() != null) {
//			
//		}
//		for (int i = 0; i < howMany; i++) {
//			Location loc = Location.get(Random.nextInt(Location.size()));
//			EventModel e = createRandomEventInLocation(loc);
//			e.setStartDateTime(new DateTime(begin));
//			e.setEndDateTime(new DateTime(end));
//			events.add(e);
//		}
//		return events;
//	}
}
