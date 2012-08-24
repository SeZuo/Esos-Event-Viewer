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

import java.util.Iterator;
import java.util.LinkedList;

import com.eSOS.eventViewer.client.control.LocationTuple;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A dynamic enum giving the locations
 * @note this follows the singleton pattern and is a rework of the enumeration pattern, thus a good example for further work
 * @author Sebastien Zurfluh
 */
public class Location implements IsSerializable, Iterable<Location>, Comparable<Location> {
	private static final long serialVersionUID = 1L;
	
	private static int staticOrdinal = -1;
	private int ordinal = staticOrdinal++;
	private String value = "instance";
	
	//* needed for iteration
	private static int locations_number = 0;
	private static int iterator = 0;
	//* end of needed for iteration
	
	private static Location INSTANCE = new Location();
	
	
	
	private Location () {
	}
	
	private Location(String name) {
		this.value = name;
	}
	
	public Location(String name, String servId) {
		this(name);
		this.servId = servId;
	}

	private static LinkedList<Location> locations = new LinkedList<Location>();
	
	private static boolean isInitialised = false;
	public static Location getInstance() {
		assert isInitialised;
		return INSTANCE;
	}
	
	/**
	 * Location must be initialised before being used
	 * It can only be initialised once
	 * @param locationsNames, a non-empty list. Try to use a list that keeps config's order
	 */
	//TODO find a way to check for empty case
	public static void initialise(Iterable<LocationTuple> locationsNames) {
		assert !isInitialised;
//		assert !locationsNames.isEmpty();
		
		for (LocationTuple locationName : locationsNames) {
			locations.add(new Location(locationName.getName(),locationName.getServId()));
		}
		isInitialised = true;
		/* needed for iteration
		 * this only work if location is initialised once and left untouched
		 * which is the case for now
		 */
		locations_number = locations.size();
	}
	
	/* Albeit this should prevent from cloning, there seems
	 *  to be an error at compilation. Apparently, the 
	 *  emulation of Object in GWT does not implement 
	 *  Cloneable and therefore no clone is permitted by default. */
//	@Override
//	protected Object clone() throws CloneNotSupportedException {
//		throw new CloneNotSupportedException();
//	}

	@Override
	public String toString() {
		return value;
	}
	
	/*
	 * This is obviously not the good way to compare locations.
	 * But there seems to be a problem with GWT way to compare
	 * objects.
	 * note: change this and you'll have to change get(String) also
	 */
	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(Location.class))
			return false;
		if (this.value.equals( ((Location)obj).toString()))
			return super.equals(obj);
		return false;
	}
	

	@Override
	public int compareTo(Location arg0) {
		if (this.equals(arg0))
			return 0;
		else
			if (this.ordinal < arg0.ordinal)
				return -1;
			else
				return 1;
	}
	
	/**
	 * 
	 * @return true if there is at least another element in the list
	 */
	public static boolean hasNext() {
		if (iterator < locations_number) {
			return true;
		} else {
			iterator = 0;
			return false;
		}
	}
	
	/**
	 * 
	 * @return the next element in the locations list
	 */
	public static Location next() {
		Location loc = locations.get(iterator);
		iterator++;
		return loc;
	}
	
	public static Location get(int ordinal) {
		Location solution = Location.getInstance();
		while(Location.hasNext()) {
			solution = Location.next();
			if (solution.ordinal == ordinal)
				break;
		}
		Location.terminateIteration();
//		assert (!solution.equals(Location.getInstance()) || index==-1);
		return solution;
	}
	public static int getOrdinal(Location location) {
		return location.ordinal;
	}
	/**
	 * 
	 * @param location's name 
	 * @return location corresponding to the given argument
	 */
	// TODO careful with this as it's using the stupid equals defined above
	// (with comparison of values instead of objects...)
	public static Location get(String location) {
		return get(getOrdinal(new Location(location)));
	}
	
	/**
	 * Used to terminate an iteration (as there is no iterator each iteration MUST be complete)
	 */
	public static void terminateIteration() {
		iterator = 0;
	}
	
	/**
	 * 
	 * @return the number of locations
	 */
	public static int size() {
		/* note: this only works as long as the number 
		 * of location does not change during runtime */
		return locations_number;
	}
	
	/**
	 * Don't use this it's not implemented yet
	 */
	@Override
	public Iterator<Location> iterator() {
		// TODO I'll see this later
		return null;
	}
	
	private String servId = "";
	/**
	 * This gives the id associated with this location on the server
	 * @return the id associated with this location on the server
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
