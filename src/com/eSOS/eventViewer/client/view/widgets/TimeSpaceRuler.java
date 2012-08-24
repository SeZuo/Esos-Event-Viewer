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
import org.gwttime.time.Minutes;
import org.gwttime.time.Period;

import com.eSOS.eventViewer.client.control.TimeSpacePresenter;
import com.eSOS.eventViewer.client.model.TimeSpaceModel;
import com.eSOS.eventViewer.client.view.TimeSpaceView;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.gen2.picker.client.DateTimePicker;
import com.google.gwt.gen2.picker.client.TimePicker;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Create the ruler showing dates and times	
 * @author Sebastien Zurfluh
 */
public class TimeSpaceRuler extends AbsolutePanel {	
	public static int height = 25;
	TimeSpaceModel timeSpaceModel;
	TimeSpacePresenter timeSpacePresenter;
	
	/**
	 * Ruler to mark dates and time. It also provides a simple navigation interface.
	 * Use update to populate the widget
	 * Don't forget to register the presenter after creating the object
	 * @param timeSpaceModel
	 * @param timeSpacePresenter
	 */
	public TimeSpaceRuler(TimeSpaceModel timeSpaceModel) {
		this.setStyleName("timeSpaceRuler");
//		this.getElement().setAttribute("style", "overflow:visible");
		this.timeSpaceModel = timeSpaceModel;
		this.setHeight(height+"px");
	}
	/**
	 * Register the presenter (mandatory)
	 * @param timeSpacePresenter
	 */
	public void registerPresenter(TimeSpacePresenter timeSpacePresenter) {
		this.timeSpacePresenter = timeSpacePresenter;
	}
	
	/**
	 * Updates everything
	 */
	public void update() {
		this.clear();
		
		this.setPixelSize(timeSpaceModel.getDraggableAreaWidth(), height);

		// Choose format for strings on both lines
		String firstLineFormat = "MM/YYYY";
		String secondLineFormat = "dd";
		int firstLinePeriod = 4;
		int secondLinePeriod = 2;
		DateTime date = new DateTime(timeSpaceModel.getRange().getStart());
		Minutes timeIntervalMinutes = Minutes.minutes(timeSpaceModel.getTimeIntervalMinutes());
		Period period = new Period(timeIntervalMinutes.toStandardDuration());
		if (period.getYears()>1) {
			firstLineFormat = "YYYY";
			secondLineFormat = "MM";
			firstLinePeriod = 4;
			secondLinePeriod = 2;
		} else if (period.getMonths()>1) {
			firstLineFormat = "MM/YYYY";
			secondLineFormat = "WW";
			firstLinePeriod = 4;
			secondLinePeriod = 2;
		} else if (period.getWeeks()>1) {
			firstLineFormat = "MM/YYYY";
			secondLineFormat = "dd";
			firstLinePeriod = 4;
			secondLinePeriod = 2;
		} else if (period.getDays()>1) {
			firstLineFormat = "MM/YYYY";
			secondLineFormat = "dd";
			firstLinePeriod = 4;
			secondLinePeriod = 2;
		} else if (period.getHours()>1) {
			firstLineFormat = "dd/MM/YYYY";
			secondLineFormat = "HH:MM";
			firstLinePeriod = 4;
			secondLinePeriod = 2;
		}  else if (period.getMinutes()>1) {
			firstLineFormat = "dd/MM/YYYY";
			secondLineFormat = "HH:MM:SS";
			firstLinePeriod = 4;
			secondLinePeriod = 4;
		}
		
		Image vBar = new Image("resources/images/mine/pix/black.gif");
		add(vBar, 0, 0);
		vBar.setVisible(false);
		vBar.setStyleName("vertical-line");
		vBar.setHeight((timeSpaceModel.getBoundaryHeight()+13)+"px");
		
		MouseOverHandler mouseOverHandler = new LabelMouseOverHandler(vBar);
		MouseOutHandler mouseOutHandler = new LabelMouseOutHandler(vBar);
		int left = 0;
		int i = 0;
		String firstLineString ="";
		String secondLineString ="";
		ControlableLabel lastDate = new ControlableLabel();
		HandlerRegistration a= lastDate.addMouseOverHandler(mouseOverHandler);
		HandlerRegistration b= lastDate.addMouseOutHandler(mouseOutHandler);

		while(left < timeSpaceModel.getDraggableAreaWidth()) {
			if(i%4==2) { //begin on the 3rd
				Image bg = new Image("resources/images/mine/pix/violet.gif");
				add(bg,left,height/2);
				bg.setPixelSize(timeSpaceModel.getTimeIntervalPixels()*2, height/2);
			}
			if(i%firstLinePeriod==0) {
				String dateString = date.toString(firstLineFormat);
				if (!dateString.equals(firstLineString)) {
					ControlableLabel timeDate = new ControlableLabel();
					timeDate.setText(dateString);
					add(timeDate, left, 0);
					timeDate.setStylePrimaryName("timeSpaceRulerLabel");
					if (i!=0) {
						a = timeDate.addMouseOverHandler(mouseOverHandler);
						b = timeDate.addMouseOutHandler(mouseOutHandler);
					} else {
						timeDate.addStyleName("timeSpaceRulerLabel-controlable");
						timeDate.addMouseDownHandler(new ControlableLabelMouseDownHandler(true));
					}
					lastDate = timeDate;
					firstLineString = dateString;
				}
			}
			if(i%secondLinePeriod==0) {
				String dateString = date.toString(secondLineFormat);
				if (!dateString.equals(secondLineString)) {
					ControlableLabel timeDate = new ControlableLabel();
					timeDate.setText(dateString);
					add(timeDate, left, height/2);
					timeDate.setStylePrimaryName("timeSpaceRulerLabel");
					timeDate.addMouseOverHandler(mouseOverHandler);
					timeDate.addMouseOutHandler(mouseOutHandler);
					secondLineString = dateString;
				}
			}
			i++;
			left+=timeSpaceModel.getTimeIntervalPixels();
			date = date.plus(timeIntervalMinutes);
		}
		a.removeHandler();
		b.removeHandler();
		lastDate.addStyleName("timeSpaceRulerLabel-controlable");
		lastDate.addMouseDownHandler(new ControlableLabelMouseDownHandler(false));
	}
	
	/**
	 * A label associated with a DateTime
	 * @author Sebastien Zurfluh
	 *
	 */
	class ControlableLabel extends Label {
		private DateTime value;
		
		public ControlableLabel(String text, DateTime dateTime) {
			super(text);
			value = dateTime;
		}
		public ControlableLabel(String text) {
			super(text);
		}
		public ControlableLabel() {
			super();
		}
		
		public void setValue(DateTime dateTime) {
			this.value = dateTime;
		}
		public DateTime getValue() {
			return value;
		}
	}
	
	class LabelMouseOverHandler implements MouseOverHandler {
		Image vBar;
		
		public LabelMouseOverHandler(Image vBar) {
			this.vBar = vBar;
		}
		
		@Override
		public void onMouseOver(MouseOverEvent event) {
			ControlableLabel sourceTimeLabel = ((ControlableLabel) event.getSource());
			
			// move the bar to the right place
			((TimeSpaceRuler)vBar.getParent()).setWidgetPosition(vBar, 
					(sourceTimeLabel.getAbsoluteLeft()-sourceTimeLabel.getParent().getAbsoluteLeft()),
					13);
			
			// set bar visible
			vBar.setVisible(true);
			((ControlableLabel) event.getSource()).addStyleDependentName("border");
		}
	}
	
	class LabelMouseOutHandler implements MouseOutHandler {
		Image vBar;
		
		public LabelMouseOutHandler(Image vBar) {
			this.vBar = vBar;
		}
		
		@Override
		public void onMouseOut(MouseOutEvent event) {
			// set bar invisible
			vBar.setVisible(false);
			((ControlableLabel) event.getSource()).removeStyleDependentName("border");
		}
	}
	
	/**
	 * Usage: create a different handler for every widget
	 * @author Sebastien Zurfluh
	 *
	 */
	class ControlableLabelMouseDownHandler implements MouseDownHandler {
		boolean isOpen = false;
		boolean firstOpen = true;
		boolean isFirst;
		DateTimePicker dateTimePicker;
		
		public ControlableLabelMouseDownHandler(Boolean isFirst) {
			this.isFirst=isFirst;
		}
		
		
		@Override
		public void onMouseDown(MouseDownEvent event) {
			if (firstOpen) {
				firstOpen = false;
				isOpen = true;
				
				ControlableLabel sourceTimeLabel = ((ControlableLabel) event.getSource());
				
				DatePicker dp = new DatePicker();
				TimePicker tp = new TimePicker(true);
				dateTimePicker = new DateTimePicker(dp, tp);
				
				
				ValueChangeHandler<Date> valueChangeHandler = new ValueChangeHandler<Date>() {
				      public void onValueChange(ValueChangeEvent<Date> event) {
				    	  Date date = event.getValue();

				    	  // Visual acknowledgement for the user.
				    	  String d = DateTimeFormat.getShortDateFormat().format(date);  
				    	  Info.display("Date Selected", "You selected {0}.", new Params(d));
				    	  dateTimePicker.setVisible(false);

				    	  // Ask presenter to redraw the TimeSpaceView with the new given range
				    	  DateTime dateTime = new DateTime(date);
				    	  // if it's the second one do as if it was the first one
				    	  if(!isFirst)
				    		  dateTime = dateTime.minus(timeSpaceModel.getRange().toDurationMillis());
				    	  timeSpacePresenter.requestChangeTime(dateTime);
				        }
				      };
				// Time and DatePicker are synchronised, there is no need to handle both. 
				dateTimePicker.getDatePicker().addValueChangeHandler(valueChangeHandler);
				
				((TimeSpaceRuler)sourceTimeLabel.getParent()).add(dateTimePicker,
						0,13); // this is not placing the element in it's place like it should.
				((TimeSpaceRuler)sourceTimeLabel.getParent()).setWidgetPosition(dateTimePicker, 
						(sourceTimeLabel.getAbsoluteLeft()-sourceTimeLabel.getParent().getAbsoluteLeft()),
						13);
				

				tp.setDateTime(sourceTimeLabel.getValue().toDate());
				dp.setValue(sourceTimeLabel.getValue().toDate());
				dateTimePicker.setVisible(true);
			} else if (isOpen) {
				isOpen = false;
				dateTimePicker.setVisible(false);
			} else {
				isOpen = true;
				dateTimePicker.setVisible(true);
			}
		}
	}
}