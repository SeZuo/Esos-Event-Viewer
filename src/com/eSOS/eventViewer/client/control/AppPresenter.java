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

import com.eSOS.eventViewer.client.model.TimeSpaceModel;
import com.eSOS.eventViewer.client.view.TimeSpaceView;
import com.eSOS.eventViewer.client.view.widgets.ImageButton;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class AppPresenter {
	static {
		// Things to do before running the app
		AppInitialiser.run();
		
		// Header
		ImageButton logo = new ImageButton("resources/images/mine/logo/logo.jpg",
				"resources/images/mine/logo/logo_glow.jpg");
		SimplePanel headerPanel = new SimplePanel();
		headerPanel.add(logo);
		RootPanel.get(Config.DIV_CONTAINER_ID).add(headerPanel);
	}
	
	public static void start() {
		
		// You may start several instance of TimeSpaceView. Playing with options, this will lead to
		// several different "interfaces".
		// Use deferred command in order to let the loading bar show
		Command defered = new Command() {
			@Override
			public void execute() {
				startTimeSpaceViewClassicInterface();
				AppInitialiser.progressBox.close();
			}
		};
		DeferredCommand.addCommand(defered);
	}

	public static void startTimeSpaceViewClassicInterface() {
		DateTime initialTime = new DateTime();
		
		// Create the timeSpaceModel
		TimeSpaceModel timeSpaceModel = new TimeSpaceModel();
		
		// Create view
		TimeSpaceView timeSpaceView = new TimeSpaceView(timeSpaceModel);
		
		// Create presenter
		TimeSpacePresenter timeSpacePresenter =
			new TimeSpacePresenter(timeSpaceModel, timeSpaceView);
		
		// And the tricky part: Register the presenter
		timeSpaceView.registerPresenter(timeSpacePresenter);
		
		// Synchronize with the JSON
		Interval range = Config.INITIAL_TIME_RANGE;
		timeSpaceModel.setEvents(Synchroniser.getEventsInRange(range));
		
		// Add the view to the panel = RENDERING
		RootPanel.get(Config.DIV_CONTAINER_ID).add(timeSpaceView);
		
		// must be run after rendering
		timeSpacePresenter.usePositioner(true);
		timeSpaceView.update();
	}
}