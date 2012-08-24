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

package com.eSOS.eventViewer.client.view.widgets;

import java.util.Date;

import org.gwttime.time.DateTime;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.TimeDatePicker;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;

public class ExtendedTimeDatePicker extends TimeDatePicker {
	HTML acceptMessage = new HTML();
	ListBox hours = new ListBox();
	ListBox minutes = new ListBox();

	@Override
	protected void onRender(Element target, int index) {
		super.onRender(target, index);


		HorizontalPanel flow = new HorizontalPanel();
		flow.setStyleName("timeDatePickerTimePanel");

		HTML timeSeparatorMessage = new HTML("h");
		acceptMessage.setText("<u>Change hour</u>");

		for (int i = 0; i<24; i++) {
			String txt = "";
			if(i<10)
				txt+="0";
			hours.addItem(txt+i);
		}

		for (int i = 0; i<60; i+=15) {
			String txt = "";
			if(i<10)
				txt+="0";
			minutes.addItem(txt+i);
		}

		hours.setStyleName("timeDatePickerText");
		flow.add(hours);
		timeSeparatorMessage.setStyleName("timeDatePickerText-separator");
		flow.add(timeSeparatorMessage);
		minutes.setStyleName("timeDatePickerText");
		flow.add(minutes);
		acceptMessage.setStyleName("timeDatePickerText-btn");
		acceptMessage.addMouseDownHandler(new LabelMouseDownHandler());
		flow.add(acceptMessage);

		hours.setVisible(true);
		minutes.setVisible(true);


		getElement().getChild(3).appendChild(flow.getElement());

		//removed this because i don't know why it's there... if the widget works remove this line completely
//		getElement().setAttribute("style", "z-index: 31");

//		updateTimeFields(getValue());
	}

	//------------------------------------------------------------------------------------


	private void updateTimeFields(Date date) {
		DateTime dateTime = new DateTime(date.getTime());
		int selectHours = dateTime.getHourOfDay()-1;
		int selectMinutes = (int) Math.round(dateTime.getMinuteOfHour()/15)-1;
		if (selectMinutes == 3) {
			selectMinutes=0;
			selectHours++;
			selectHours%=24;
		}
		if (selectHours>24)
			selectHours = 1;
		hours.setItemSelected(selectHours, false);
		minutes.setItemSelected(selectMinutes, false);
	}

	class LabelMouseDownHandler implements MouseDownHandler {
		@Override
		public void onMouseDown(MouseDownEvent event) {
			//			DateTime date = new DateTime(getValue().getTime());
			//			date = date.withHourOfDay(Integer.parseInt(hours.getValue(hours.getSelectedIndex())));
			//			date = date.withMinuteOfHour(Integer.parseInt(minutes.getValue(minutes.getSelectedIndex())));
			//			setValue(date.toDate());
		}

	}
}

