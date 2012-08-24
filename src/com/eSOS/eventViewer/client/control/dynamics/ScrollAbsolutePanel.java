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

package com.eSOS.eventViewer.client.control.dynamics;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class ScrollAbsolutePanel extends AbsolutePanel {
	public ScrollAbsolutePanel() {
		super(DOM.createDiv());

		// Setting the panel's position style to 'relative' causes it to be treated
		// as a new positioning context for its children.
		DOM.setStyleAttribute(getElement(), "position", "relative");
		DOM.setStyleAttribute(getElement(), "overflow", "scroll");
		getElement().setAttribute("style",
				getElement().getAttribute("style")
				+"overflow-y: hidden");
	}

	/**
	 * Gets the horizontal scroll position.
	 * 
	 * @return the horizontal scroll position, in pixels
	 */
	public int getHorizontalScrollPosition() {
		return DOM.getElementPropertyInt(getElement(), "scrollLeft");
	}
}
