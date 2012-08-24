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

import com.eSOS.eventViewer.client.view.widgets.TimeSpaceRuler.ControlableLabel;
import com.extjs.gxt.ui.client.widget.DatePicker;
import com.google.gwt.user.client.Element;

public class NewExtendedDatePicker extends DatePicker {
	ControlableLabel associatedLabel;
	
	public NewExtendedDatePicker(ControlableLabel associatedLabel) {
		super();
		this.associatedLabel = associatedLabel;
	}
	
	@Override
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		setValue(associatedLabel.getValue().toDate(),true);
	}
}
