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
import org.gwttime.time.Duration;
import org.gwttime.time.Interval;

public class Config {
	
	public native String getConfigs() /*-{
		function new_xhr() {
			var xhr_object = null;
			if(window.XMLHttpRequest) // Firefox et autres
	   			xhr_object = new XMLHttpRequest();
			else if(window.ActiveXObject) { // Internet Explorer
	   			try {
                	xhr_object = new ActiveXObject("Msxml2.XMLHTTP");
        		} catch (e) {
                	xhr_object = new ActiveXObject("Microsoft.XMLHTTP");
            	}
			}
			else { // XMLHttpRequest non supporte par le navigateur
	   			alert("Votre navigateur ne supporte pas Ajax, veuillez installer Firefox, comme tout le monde.");
	   			xhr_object = false;
			}
			return xhr_object;
		}

		function getContent(address) {
			var xhr = new_xhr();//On crée un nouvel objet XMLHttpRequest
			xhr.onreadystatechange = function() {
				if ( xhr.readyState == 4 ){// Actions executées une fois le chargement fini
					if(xhr.status  != 200){// Message si il se produit une erreur
						return "Error code " + xhr.status;
					} else {// On renvoi le contenu du fichier
						return xhr.responseText;
					}
				} else {
					// En attendant, on ne fait rien
				}
			}
			xhr.open("GET", address, true); // Appel du fichier externe
			xhr.send(null);
		}
		
		return getContent("config.xml")
	}-*/;
	
	
	static {
		//TODO use getConfigs to populate Config.java
		//load()
	}
	
	/**
	 * The id of the application's container
	 */
	public static final String DIV_CONTAINER_ID = "zion";
	/**
	 * Width of the box bounding the drag and drop functions
	 * Set a value of -1 for automatic size
	 */
	public static final int BOUNDARY_WIDTH = -1;
	
	
	/**
	 * Start the application with a time range of
	 */
	public static final Interval INITIAL_TIME_RANGE = new Interval(new DateTime(), Duration.standardDays(7));
	
	/**
	 * Initial time interval per cell in pixels 
	 */
	public static final int TIMELINE_INITIAL_INTERVAL_PIXELS = 25;
	/**
	 * Initial time interval per cell in seconds
	 */
	public static final int TIMELINE_INITIAL_INTERVAL_MINUTES = 240;
	/**
	 * Height of one line
	 */
	public static final int TIMELINE_LINE_HEIGHT = 25;
	
	
	/**
	 * List the rooms (or locations) to loan
	 * @Deprecated You should use the Synchroniser to get locations from the server
	 */
	@Deprecated
	public static final String[] LOCATIONS = {
		"Cent-Suisse",
		"Piccard",
		"Terrasse",
		"Le Fort",
		"Fontana",
		"Gallatin"};
	
	/**
	 * Gives the default number of line for each location
	 */
	public static final int DEFAULT_LOCATIONS_HEIGHT = 1;
	/**
	 * Default height of a line
	 */
	public static final  int DEFAULT_LOCATIONS_LINE_HEIGHT = 25;
	

	/**
	 * Loads the configs from a file
	 * Note: currently not implemented
	 */
	public static void load() {
		//TODO should throw some error with config file in case of error
		// and a solution should be found
		//TODO read vars from a config file
	}
	/**
	 * Saves configs to a file
	 * Note: currently not implemented
	 */
	public static void save() {
		//TODO save config vars to the config file
	}
}
