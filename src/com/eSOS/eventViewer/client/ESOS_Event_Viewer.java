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

package com.eSOS.eventViewer.client;

import com.eSOS.eventViewer.client.control.AppPresenter;
import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.DialogBox;

import com.google.gwt.event.shared.UmbrellaException;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ESOS_Event_Viewer implements EntryPoint {

	@Override
	public void onModuleLoad() {
		// set uncaught exception handler
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			String text = "Uncaught exception: ";
			public void onUncaughtException(Throwable throwable) {
				if (throwable.getClass().equals(UmbrellaException.class)) {
					UmbrellaException umbrella = (UmbrellaException)throwable;
					text += "UmbrellaException \n";
					for(Throwable umbrellaThrowable : umbrella.getCauses()) {
						printStackTrace(umbrellaThrowable);
					}
				} else {
					printStackTrace(throwable);
				}
				DialogBox dialogBox = new DialogBox(true);
				DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor", "#ABCDEF");
				DOM.setStyleAttribute(dialogBox.getElement(), "z-index", "20");
				System.err.print(text);
				text = text.replaceAll(" ", "&nbsp;");
				dialogBox.setHTML("<pre>" + text + "</pre>");
				dialogBox.center();
			}
			private void printStackTrace(Throwable throwable) {
				while (throwable != null) {
					StackTraceElement[] stackTraceElements = throwable.getStackTrace();
					text += throwable.toString() + "\n";
					for (int i = 0; i < stackTraceElements.length; i++) {
						text += "    at " + stackTraceElements[i] + "\n";
					}
					throwable = throwable.getCause();
					if (throwable != null) {
						text += "Caused by: ";
					}
				}
			}
		});

		// use a deferred command so that the handler catches exceptions
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				AppPresenter.start();
			}
		});
	}
}
