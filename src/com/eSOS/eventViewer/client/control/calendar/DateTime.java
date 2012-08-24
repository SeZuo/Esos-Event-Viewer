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

package com.eSOS.eventViewer.client.control.calendar;

import org.gwttime.time.Minutes;

/**
* Library of date functions
* 
* Usage:
* To implement this code instatiate the object.
* 
* @author    Sven Godo <sgodo@rationalselling.com>
* @version   c:05-09-2010, m:05-09-2010, mm:05-09-2010
* @package   schedule
*
* @copyright Copyright (C) 2008-2010 eSOS
* @TODO public functions can be implemented with a String return type and "return toString();"
* @Deprecated this class is shit. Use the org.gwttime one.
*/
@Deprecated
public class DateTime implements Comparable<DateTime> {

private long dateValue;

	public DateTime() {
		setToday();
	}

	public DateTime(long dateValue) {
		this.dateValue = dateValue;
	}

	@Override
	public String toString() {
		return toString(this.dateValue);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof DateTime && ((DateTime)obj).dateValue == dateValue;
	}
	
	@Override
	public int compareTo(DateTime arg0) {
		return compareTo(arg0.dateValue);
	}
	public int compareTo(Long arg0) {
		if (equals(arg0))
			return 0;
		return dateValue > arg0 ? 1 : -1;
	}
	
	public long getValue() {
		return dateValue;
	}
	
	/**
	 * This does nothing except return 0
	 * @param date
	 * @return
	 */
	@Deprecated
	public int convertFromEsosTimeFormat(int date) {
		//TODO 
		return 0;
	}
	


	public void setToday() {
		this.dateValue = (long)getToday();
	}
	
	public void dayForward(){
		this.dateValue = (long)dayForward(this.dateValue,1);
	}
	
	public void dayForward(int step){
		this.dateValue = (long)dayForward(this.dateValue,step);
	}
	
	public void dayBack(){
		this.dateValue = (long)dayBack(this.dateValue,1);
	}
	
	public void dayBack(int step){
		this.dateValue = (long)dayBack(this.dateValue,step);
	}
	
	public void dayChange(double change){
		this.dateValue = (long)dayChange(this.dateValue, change);
	}
	
	public void monthForward(){
		this.dateValue = (long)monthForward(this.dateValue);
	}
	
	public void monthBack(){
		this.dateValue = (long)monthBack(this.dateValue);
	}
	
	public void monthChange(double change){
		this.dateValue = (long)monthChange(this.dateValue, change);
	}
	
	public void yearForward(){
		this.dateValue = (long)yearForward(this.dateValue);
	}
	
	public void yearBack(){
		this.dateValue = (long)yearBack(this.dateValue);
	}
	
	public void yearChange(double change){
		this.dateValue = (long)yearChange(this.dateValue, change);
	}
	
	private static native String toString(double date) /*-{
		var d = new Date(date);
		return d.toDateString();
	}-*/;
	
	private static native double getToday() /*-{
		var d = new Date();
		return d.getTime();
	}-*/;
	
	private static native double dayForward(double date, int step)/*-{
		var d = new Date(date);
		d.setDate(d.getDate() + step);
		return d.getTime();
	}-*/;
	
	private static native double dayBack(double date, int step)/*-{
		var d = new Date(date);
		d.setDate(d.getDate() - step);
		return d.getTime();
	}-*/;
	
	/**
	 * this function is unclear compared to dayForward(int step), dayBack(int Step)
	 * @param date
	 * @param change
	 * @return
	 */
	@Deprecated
	private static native double dayChange(double date, double change) /*-{
		var d = new Date(date);
		d.setDate(d.getDate() + change);
		return d.getTime();
	}-*/;
	
	private static native double monthForward(double date)/*-{
		var d = new Date(date);
		d.setMonth(d.getMonth() + 1);
		return d.getTime();
	}-*/;
	
	private static native double monthBack(double date)/*-{
		var d = new Date(date);
		d.setMonth(d.getMonth() - 1);
		return d.getTime();
	}-*/;
	
	private static native double monthChange(double date, double change) /*-{
		var d = new Date(date);
		d.setMonth(d.getMonth() + change);
		return d.getTime();
	}-*/;
	
	private static native double yearForward(double date)/*-{
		var d = new Date(date);
		d.setFullYear()(d.getFullYear() + 1);
		return d.getTime();
	}-*/;
	
	private static native double yearBack(double date)/*-{
		var d = new Date(date);
		d.setFullYear(d.getFullYear() - 1);
		return d.getTime();
	}-*/;
	
	private static native double yearChange(double date, double change) /*-{
		var d = new Date(date);
		d.setFullYear()(d.getFullYear() + change);
		return d.getTime();
	}-*/;
	
	
	
	/**
	 * Gives the width converted in minutes
	 * @param width
	 * @return 
	 */
	public static int minutesIn(long width) {
		org.gwttime.time.Minutes minutes = org.gwttime.time.Minutes.minutesBetween(
				new org.gwttime.time.DateTime(),
				new org.gwttime.time.DateTime().plus(width));
		return minutes.getMinutes();
	}

	public enum TimeConstant {
		SECOND(1000),
		MINUTE(60*SECOND.value),
		HOUR(60*MINUTE.value),
		DAY(24*HOUR.value),
		YEAR((long)Math.floor(365.25*DAY.value));
		
		private long value;
		private TimeConstant(long milliseconds) {
			this.value = milliseconds;
		}
		@Override
		public String toString() {
			return Long.toString(value);
		}
		public long getValue() {
			return value;
		}
	}
}