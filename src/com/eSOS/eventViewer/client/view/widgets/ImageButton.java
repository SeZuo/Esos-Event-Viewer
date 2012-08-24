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

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Image;

public class ImageButton extends Image implements MouseOverHandler, MouseOutHandler, MouseDownHandler, MouseUpHandler {
	String normalURL;
	String overURL;
	String downURL;
	String disabledURL;

	Boolean enabled = true;

	/**
	 * Constructor
	 * @param normalUrl, the URL for the default image
	 */
	public ImageButton(String normalUrl) {
		normalURL = normalUrl;
		setUrl(normalURL);
		addMouseOverHandler(this);
		addMouseOutHandler(this);
		addMouseDownHandler(this);
		addMouseUpHandler(this);
	}
	/**
	 * Constructor
	 * @param normalUrl the URL for the default image
	 * @param overUrl  the URL for the image appearing on mouse overing
	 */
	public ImageButton(String normalUrl, String overUrl) {
		this(normalUrl);
		overURL = overUrl;
	}
	/**
	 * Constructor
	 * @param normalUrl the URL for the default image
	 * @param overUrl  the URL for the image appearing on mouse overing
	 * @param downUrl the URL to display when the button is pressed down
	 */
	public ImageButton(String normalUrl, String overUrl, String downUrl) {
		this(normalUrl, overUrl);
		downURL = downUrl;
	}
	/**
	 * Constructor
	 * @param normalUrl the URL for the default image
	 * @param overUrl  the URL for the image appearing on mouse overing
	 * @param disabledUrl the URL for the image disabled
	 * @param downUrl the URL to display when the button is pressed down
	 */
	public ImageButton(String normalUrl, String overUrl, String downUrl, String disabledUrl) {
		this(normalUrl, overUrl, downUrl);
		disabledURL = disabledUrl;
	}

	public void setImageOver (String url) {
		overURL = url;
	}

	public void setImageDisabled (String url) {
		overURL = url;
	}


	public void enable() {
		if (enabled)
			return;
		enabled=true;
		if (disabledURL != null)
			setUrl(normalURL);
	}
	public void disable() {
		if (!enabled)
			return;
		enabled=false;
		if (disabledURL != null)
			setUrl(disabledURL);
	}

	public void setTooltip(String tipText) {
		setTitle(tipText);
	}




	@Override
	public void onMouseOver(MouseOverEvent event) {
		if (enabled && overURL != null) {
			setUrl(overURL);
		}
	}
	@Override
	public void onMouseOut(MouseOutEvent event) {
		if (enabled && overURL != null | downURL != null ) {
			setUrl(normalURL);
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (enabled && downURL != null) {
			setUrl(downURL);
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (enabled && downURL != null) {
			if (overURL != null) {
				setUrl(overURL);
			} else {
				setUrl(normalURL);
			}
		}
	}
	
}
