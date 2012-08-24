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

import java.util.LinkedList;
import com.eSOS.eventViewer.client.model.Location;
import com.extjs.gxt.ui.client.widget.MessageBox;


/**
 * Do the stuff needed before running the application
 * @author Sebastien Zurfluh
 */
public class AppInitialiser {
	public static MessageBox progressBox;
	
	/**
	 * Do the stuff needed before running the application
	 * <li> initialise locations
	 */
	public static void run() {
		 MessageBox box = new MessageBox();
		 box.setTitle("Initialising");
		 box.setMessage("Please wait...");
		 box.setButtons("");
		 box.setClosable(false);
		 box.show();
		 progressBox = box;
		    
//		final MessageBox box = MessageBox
//        .progress("Please wait", "Loading items...", "Initializing...");
//		final ProgressBar bar = box.getProgressBar();
//		final Timer t = new Timer() {
//			float i;
//
//			@Override
//			public void run() {
//				bar.updateProgress(i / 100, (int) i + "% Complete");
//				i += 5;
//				if (i > 105) {
//					cancel();
//					box.close();
//					Info.display("Message", "Items were loaded", "");
//				}
//			}
//		};
//		t.scheduleRepeating(1);
		
		// Load configurations
		Config.load();
		
		// Initialise locations
		Location.initialise(Synchroniser.getLocations());
	}
	
	/**
	 * @Deprecated use Synchroniser.getLocations() instead
	 * @return
	 */
	@Deprecated
	private static LinkedList<String> getLocations() {
		LinkedList<String> locationSet = new LinkedList<String>();
		for (String locationName : Config.LOCATIONS) {
			locationSet.add(locationName);
		}
		return locationSet;
	}
}
