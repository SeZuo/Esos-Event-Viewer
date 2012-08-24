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

import com.eSOS.eventViewer.client.model.TimeSpaceModel;
import com.eSOS.eventViewer.client.view.TimeSpaceView;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DragEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.event.WidgetListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Slider;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * This class provides the widget for controlling grain (precision) and zoom
 * Note: this widget controls directly the view updates
 * @author Sebastien Zurfluh
 *
 */
public class ZoomAndPrecision extends HorizontalPanel {
	TimeSpaceModel timeSpaceModel;
	TimeSpaceView timeSpaceView;
	
	public ZoomAndPrecision(final TimeSpaceModel timeSpaceModel, final TimeSpaceView timeSpaceView) {
		this.timeSpaceModel = timeSpaceModel;
		this.timeSpaceView  = timeSpaceView;
	}
	
	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);
		
		
		Label zoomLabel = new Label("zoom");
		zoomLabel.setStyleName("zoomAndPrecisionLabel");
		HorizontalPanel zoomPanel = new HorizontalPanel();
		ZoomSlider zoom = new ZoomSlider();
		zoom.setClickToChange(false);
		zoom.setMessage("{0}%");
		zoom.setWidth("100px");
		zoom.setIncrement(100);
		zoom.setMinValue(100);
		zoom.setMaxValue(1600);
		
		
		zoomPanel.add(zoomLabel);
		zoomPanel.add(zoom);
		
		
		Label precisionLabel = new Label("precision");
		precisionLabel.setStyleName("zoomAndPrecisionLabel");
		HorizontalPanel precisionPanel = new HorizontalPanel();
		PrecisionSlider precision = new PrecisionSlider();
		precision.setClickToChange(false);
		precision.setMessage("{0} minutes");
		precision.setWidth("100px");
		precision.setIncrement(5);
		precision.setMinValue(5);
		precision.setMaxValue(240);
		precision.setValue(timeSpaceModel.getTimeIntervalMinutes());
		
		precisionPanel.add(precisionLabel);
		precisionPanel.add(precision);
		
		Image spacer = new Image("resources/images/mine/pix/white.gif");
		spacer.setWidth("7px");
		add(zoomPanel);
		add(spacer);
		add(precisionPanel);
	}
	
	class ZoomSlider extends Slider {
		int startValue=0;
		int endValue=0;
		
		@Override
		protected void onDragStart(DragEvent de) {
			startValue = this.getValue();
			super.onDragStart(de);
		}

		@Override
		protected void onDragEnd(DragEvent de) {
			endValue = this.getValue();
			super.onDragEnd(de);
			
			if (startValue != endValue) {
				
				if(startValue == 0)
					Info.display("startView","0");

				if(endValue == 0)
					Info.display("endView","0");
				
				if(timeSpaceModel.getTimeIntervalPixels() == 0)
					Info.display("TimeIntervalPixel","0");
				
				// Do the calculus in double for a better precision (fuck GWT)
				double a = endValue * timeSpaceModel.getTimeIntervalPixels() / startValue ;
				timeSpaceModel.setTimeIntervalPixels((int)a);
				
				Info.display("Zoom", ""+timeSpaceModel.getTimeIntervalPixels());
				// partial update since there's no need to update the location grid
				timeSpaceView.update();
			}
		}
	}
	
	class PrecisionSlider extends Slider {
		int startValue=0;
		int endValue=0;
		
		@Override
		protected void onDragStart(DragEvent de) {
			timeSpaceView.loading.setVisible(true);
			startValue = this.getValue();
			super.onDragStart(de);
		}

		@Override
		protected void onDragEnd(DragEvent de) {
			endValue = this.getValue();
			super.onDragEnd(de);
			
			if (startValue != endValue) {
				timeSpaceModel.setTimeIntervalMinutes( this.getValue() );
				Info.display("Precision change","You've set a precision of "+timeSpaceModel.getTimeIntervalMinutes());
				
				// partial update since there's no need to update the location grid
				timeSpaceView.updateTimeGridDimensions();
				timeSpaceView.updateBoundaryDimensions();
				timeSpaceView.updateEvents();
			}
			timeSpaceView.loading.setVisible(false);
		}
	}
}
