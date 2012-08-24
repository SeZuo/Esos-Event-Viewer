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

package com.eSOS.eventViewer.client.jUnit;



import com.eSOS.eventViewer.client.model.EventModel;
import com.eSOS.eventViewer.client.view.EventView;
import com.extjs.gxt.ui.client.widget.Label;
import com.google.gwt.user.client.ui.FlowPanel;

public class Test extends FlowPanel {

	
	public Test() {
		this.add(new Label("my Panel"));
//		DateTime dt = new DateTime(2010,8,1,19,0,0,0);
//		EventModel em = new EventModel("Event Name",
//				new DateTime(2010,8,1,12,0,0,0),
//				new DateTime(2010,8,1,19,0,0,0),
//				"Erlach");
//		this.add(new EventView(em));
	}
}
