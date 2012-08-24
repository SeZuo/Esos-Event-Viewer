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

package com.eSOS.eventViewer.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.eSOS.eventViewer.client.control.Config;
import com.eSOS.eventViewer.client.control.TimeSpacePresenter;
import com.eSOS.eventViewer.client.control.dynamics.GridConstrainedDropController;
import com.eSOS.eventViewer.client.control.dynamics.HasPositioner;
import com.eSOS.eventViewer.client.control.dynamics.ScrollAbsolutePanel;
import com.eSOS.eventViewer.client.control.dynamics.WidgetPositioner;
import com.eSOS.eventViewer.client.jUnit.EventFactory;
import com.eSOS.eventViewer.client.model.EventModel;
import com.eSOS.eventViewer.client.model.Location;
import com.eSOS.eventViewer.client.model.TimeSpaceModel;
import com.eSOS.eventViewer.client.view.widgets.TimeSpaceRuler;
import com.eSOS.eventViewer.client.view.widgets.TimeSpaceTools;
import com.eSOS.eventViewer.client.view.widgets.ZoomAndPrecision;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.IncrementalCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * This is the view for the Events container.
 * It does provide the grid to which the events will be snapping.
 * It creates the panels (divs) for the TimeSpaceRuler widget and TimeSpaceTools.
 * 
 * @author Sebastien Zurfluh
 *
 */
public class TimeSpaceView extends SimplePanel implements HasPositioner {
	GridConstrainedDropController dropController;
	TimeSpaceModel model;
	TimeSpaceRuler timeSpaceRuler;
	
	// Panels
	HorizontalPanel hpane;
	ScrollAbsolutePanel boundaryPanel;
	AbsolutePanel targetPanel;
	AbsolutePanel toolPanel;

	// DND
	PickupDragController dragController;
	
	// some widget
	public Image loading; 

	//------------------------------------------------------------------------------------
	// Fake event bus
	
	
	
	// TODO change this with an event bus
	/**
	 * Don't use this until the registration is done !!! You've been warned.
	 */
	private TimeSpacePresenter tsp;
	/**
	 * This boolean permits to check if the tsp is properly registered
	 * (debug purpose)
	 */
	private boolean isRegistered = false;
	// TODO change this with an event bus
	public void registerPresenter(TimeSpacePresenter tsp) {
		assert isRegistered == false;
		this.tsp = tsp;
		this.isRegistered = true;
		timeSpaceRuler.registerPresenter(tsp);
	}
	
	//------------------------------------------------------------------------------------
	// HasPositioner implementation
	
	
	
	public boolean hasPositioner() {
		return this.hasPositioner;
	}
	
	private WidgetPositioner<EventView> positioner =
		new WidgetPositioner<EventView>(null,null);
	/**
	 * This boolean permits to check if the WidgetPositionner is registered
	 * (debug purpose)
	 */
	private boolean hasPositioner = false;
	
	public void registerPositioner(WidgetPositioner<EventView> positioner) {
		this.positioner = positioner;
		// this is used by asserts (debug purpose)
		this.hasPositioner = true;
		dropController.registerPositioner(positioner);
	}
	public void removePositioner() {
		this.hasPositioner = false;
		dropController.removePositioner();
	}
	
	//------------------------------------------------------------------------------------
	// Constructors
	
	
	
	/**
	 * Create the object.
	 * Note: update() after register() in order to complete output
	 * Note2: any size would do when creating the different controllers
	 * as the final size will be updated after rendering the object
	 * @param model
	 */
	public TimeSpaceView(TimeSpaceModel model) {
		this.model = model;
		

		setStyleName("outerbound");

		// Create a horizontal panel containing everything
		hpane = new HorizontalPanel();
		hpane.setWidth("100%");

		// Create a boundary panel to constrain all drag operations
		boundaryPanel = new ScrollAbsolutePanel();
		boundaryPanel.setPixelSize(model.getBoundaryWidth(),
				model.getBoundaryHeight()+scrollBarWidth+1); //+1 is the targetPanel's fucking border
		boundaryPanel.addStyleName("boundarypanel");

		// Create a drop target on which we can drop events
		targetPanel = new AbsolutePanel();
		targetPanel.setPixelSize(model.getDraggableAreaWidth(),
				model.getBoundaryHeight());
		targetPanel.addStyleName("time-line");
		// Create the drop target's ruler;
		timeSpaceRuler = new TimeSpaceRuler(model);
		
		//TODO This fixes a bug (probably overrides some settings elsewhere!). Find where.
		// begin of bug fix
		timeSpaceRuler.getElement().getStyle().setPosition(Position.RELATIVE);
		timeSpaceRuler.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		timeSpaceRuler.getElement().getStyle().setZIndex(30);
		// end of bug fix
		
		// Zoom and grain tools
		ZoomAndPrecision zoom = new ZoomAndPrecision(model, this);
		zoom.setHorizontalAlign(HorizontalAlignment.RIGHT);
		
		// Loading animation
		loading = new Image("resources/images/mine/loading.gif");
		loading.setVisible(false);
		
		// Create the "toolLine", containing the ZoomAndPrecision widget and the loading Image
		HorizontalPanel toolLine = new HorizontalPanel();
		Image spacer = new Image("resources/images/mine/pix/white.gif");
		spacer.setWidth("7px");
		toolLine.add(zoom);
		toolLine.add(spacer);
		toolLine.add(loading);
		
		
		// Create a vertical pane to wrap the targetPanel and it's ruler together
		VerticalPanel vpane = new VerticalPanel();
		vpane.add(timeSpaceRuler);
		vpane.add(targetPanel);
		
		//Create a vertical pane to wrap the tool line
		VerticalPanel vpane2 = new VerticalPanel();
		vpane2.add(toolLine);
		
		// Create a panel to contain TimeSpaceTools
		toolPanel = new AbsolutePanel();
		toolPanel.setPixelSize(20,
				model.getBoundaryHeight()+TimeSpaceRuler.height+1); //+1 is the targetPanel's fucking border

		// Add all panels to the simple panel
		hpane.add(boundaryPanel);
		hpane.add(toolPanel);
		vpane2.add(hpane);
		add(vpane2);

		boundaryPanel.add(vpane, 0, 0);
		
		

		// Create a DragController for each logical area where a set of
		// draggable
		// widgets and drop targets will be allowed to interact with one
		// another.
		dragController = new PickupDragController(boundaryPanel, false);

		// Positioner is always constrained to the boundary panel
		// Use 'true' to also constrain the draggable or drag proxy to the
		// boundary panel
		dragController.setBehaviorConstrainedToBoundaryPanel(false);

		// Allow multiple widgets to be selected at once using CTRL-click
		dragController.setBehaviorMultipleSelection(false);

		dragController.setBehaviorScrollIntoView(true);

		// create a DropController for each drop target on which draggable
		// widgets
		// can be dropped
		dropController = new GridConstrainedDropController(targetPanel,
				model.getTimeIntervalPixels(),
				model.getLineHeight());	

		// Register each DropController with a DragController
		dragController.registerDropController(dropController);
	}

	//------------------------------------------------------------------------------------
	// Boundaries update
	
	
	
	private native static int getScrollBarWidth() /*-{  
		var inner = document.createElement('p');  
		inner.style.width = "100%";  
		inner.style.height = "200px";  

		var outer = document.createElement('div');  
		outer.style.position = "absolute";  
		outer.style.top = "0px";  
		outer.style.left = "0px";  
		outer.style.visibility = "hidden";  
		outer.style.width = "200px";  
		outer.style.height = "150px";  
		outer.style.overflow = "hidden";  
		outer.appendChild (inner);  

		document.body.appendChild (outer);  
		var w1 = inner.offsetWidth;  
		outer.style.overflow = 'scroll';  
		var w2 = inner.offsetWidth;  
		if (w1 == w2) w2 = outer.clientWidth;  

		document.body.removeChild (outer);  
		return (w1 - w2);  
	}-*/;
	/**
	 * Scroll bar width (should only be calculated once)
	 */
	private static int scrollBarWidth = getScrollBarWidth();
	
	public void updateBoundaryDimensions() {
		
		// Please do not consider warnings about identical expressions. If there is dead code, it is meant so.
		int boundaryWidth = (Config.BOUNDARY_WIDTH == -1) ?
				RootPanel.get(Config.DIV_CONTAINER_ID).getOffsetWidth()-50
				: model.getBoundaryWidth();
		
		boundaryPanel.setPixelSize(boundaryWidth,
				model.getBoundaryHeight()+scrollBarWidth+TimeSpaceRuler.height+1);
		targetPanel.setPixelSize(model.getDraggableAreaWidth(),
				model.getBoundaryHeight());
		toolPanel.setPixelSize(20,
				model.getBoundaryHeight()+TimeSpaceRuler.height+1);
		
	}

	//------------------------------------------------------------------------------------
	// Location grid
	
	
	
	/**
	 * Inherent to updateLocationGridDimensions()
	 */
	private HashMap<Location, Label> labels = new HashMap<Location, Label>();
	private HashMap<Location, Image> lines = new HashMap<Location, Image>();
	//TODO this is populated in a weird way... corrections needed
	private HashMap<Location, TimeSpaceTools> tools = new HashMap<Location, TimeSpaceTools>();
	/**
	 * Updates the dimension of the visible location grid and 
	 * the position for tools accordingly
	 * Actually, this does also create the needed objects at first run
	 * or in case of erroneous garbage collection (yeah that's a feature)
	 * Note: this should always be called before boundary updates
	 * as it modify their values
	 */
	public void updateLocationGridDimensions() {
		assert (isRegistered == true);

		
		HashMap<Location, Integer> locationHeights = model.getLocationHeights();
		
		int elementPositionFromTop = 0;
		
		
		
		while (Location.hasNext()) {
			Location loc = Location.next();
			
			// text should be placed above the line
			if (!labels.containsKey(loc)) {
				Label locationText = new Label(loc.toString());
				targetPanel.add(locationText);
				locationText.setStyleName("locationgrid-text");

				labels.put(loc, locationText);
			}
			labels.get(loc).getElement().setAttribute(
					"style", "top:"+(elementPositionFromTop)+"px;font-size:"+
					(locationHeights.get(loc)*model.getLineHeight()-8)+"px");
				
			
			// then update the toolbox (TimeSpaceTools) position to match the labels
			if (!tools.containsKey(loc)) {
				TimeSpaceTools timeSpaceTools = new TimeSpaceTools(tsp,loc);
				toolPanel.add(timeSpaceTools,0,0);
				tools.put(loc, timeSpaceTools);
			}
			tools.get(loc).getElement().setAttribute(
					"style", "overflow:visible;" +
							"position:absolute;" +
							"top:"+(elementPositionFromTop+TimeSpaceRuler.height)+"px;");
			// note: here we have to add the TimeSpaceRuler.height
			
			// here we set the new line position
			elementPositionFromTop += locationHeights.get(loc)*model.getLineHeight();
			
			// now the line
			if (!lines.containsKey(loc)) {
				Image line = new Image("resources/images/mine/pix/red.gif");
				targetPanel.add(line);
				line.setStyleName("horizontal-line");
				lines.put(loc, line);
			}
			lines.get(loc).getElement().setAttribute(
					"style", "top: "+elementPositionFromTop+"px"
					+";width: "+model.getDraggableAreaWidth()+"px");
		}
		// TODO fire boundary change
	}

	//------------------------------------------------------------------------------------
	// Time grid update
	
	
	
	public void updateTimeGridDimensions() {
		updateGridDimensions();
		updateCellInterval();

		timeSpaceRuler.update();
	}
	
	/**
	 * Updates the dimension of the invisible drop grid
	 */
	private void updateGridDimensions() {

		dropController.changeGrid(model.getTimeIntervalPixels(),
				model.getLineHeight());

	}
	
	/**
	 * Used by updateCellInterval
	 */
	private LinkedList<Image> bgFillList = new LinkedList<Image>();
	/**
	 * Used by updateCellInterval
	 */
	private int timeIntervalNumber = 0;
	/**
	 * Updates the visible columns according to the invisible grid dimension
	 */
	private void updateCellInterval() {
		Integer modelGetTimeIntervalNumber = model.getTimeIntervalNumber();
		if (timeIntervalNumber < modelGetTimeIntervalNumber) {
			// smaller: create more elements
			for(int i = timeIntervalNumber; i < modelGetTimeIntervalNumber; i++) {
				createBGFillImage();
			}
		} else { // bigger: reduce the element set
			for(int i = timeIntervalNumber; i < modelGetTimeIntervalNumber; i++) {
				removeBGFillImage();
			}
		}
		timeIntervalNumber = modelGetTimeIntervalNumber;
		// now the number of elements is right, position and size is left
		int pos = 0;
		for (Image bgImg : bgFillList) {
			int timeIntervalPixels = model.getTimeIntervalPixels();
			addStyleToImage(bgImg, pos, timeIntervalPixels);
			pos += 4 * timeIntervalPixels;
		}
	}
	/**
	 * Used by updateCellInterval
	 * Create the image object
	 */
	private void createBGFillImage() {
		Image bgfill = new Image("resources/images/mine/whitepix.gif");
		targetPanel.add(bgfill);
		bgFillList.add(bgfill);
	}
	/**
	 * Used by updateCellInterval
	 * Delete an image object
	 */
	private void removeBGFillImage() {
		targetPanel.remove(bgFillList.getFirst());
		bgFillList.removeFirst();
	}
	/**
	 * Used by updateCellInterval
	 * Add the style property to the image object.
	 */
	private void addStyleToImage(Image img, int position, int timeIntervalPixels) {
		img.setStyleName("cellInterval");
		img.getElement().setAttribute(
				"style",
				"left: " + position + "px; width: " + 2
				* timeIntervalPixels + "px");
	}
	
	//------------------------------------------------------------------------------------
	// Event update
	
	
	
	/**
	 * List events' views.
	 */
	//TODO verify if this is the right place for this
	// for now it seems right as this is view logic (needed for the positioner)
	private ArrayList<EventView> eventViews = new ArrayList<EventView>();
	public ArrayList<EventView> getEventViews() {
		return eventViews;
	}
	/**
	 * This is used by the updateEvents() delayed (deferred) parts. It is used to cancel the loading of those events.
	 */
	private boolean stopToLoadEvents = false;
	private void stopToLoadEvents() {
		stopToLoadEvents = true;
	}
	/**
	 * This updates the events
	 * Careful: this use a deferred command and could be executed out of order
	 */
	public void updateEvents() {
		loading.setVisible(true);
		
		// don't remove the if... stopToLoadEvents() prevent the next element in the deferredcommand list to happen...
		// meaning that if no elements are in there yet, something bad will happen to him!
		if(!getEventViews().isEmpty()) {
			// stop the rendering of previous events!
			stopToLoadEvents();
			// delete the view and data of the previously rendered events
			clearEvents();
		}
		
		int topRelativeDifference = getEventRelativeTopDifference();
		int leftRelativeDifference = getEventRelativeLeftDifference();
		positioner.setRelativeReferential(leftRelativeDifference, topRelativeDifference);
		
		// This IncrementalCommand truncate the code flow is small pieces, helping loading it.
		IncrementalCommand ic = new IncrementalCommand(){
			Iterator<EventModel> it = model.getEvents().iterator();

			public boolean execute() {
				// If an external instance is breaking the loop
				if(stopToLoadEvents) {
					stopToLoadEvents = false;
					return false;
				}
				
				// If we have done all the work, exit with a 'false'
				// return value to terminate further execution.
				if (!it.hasNext()) {
					// end of computation processing ...
					loading.setVisible(false);
					return false;
				} else {
					// do what has to be done
					buildEventView(it.next());
					// Call the execute function again.
					return true;
				}
			}
		};

		// Schedule the IncrementalCommand instance to run when
		// control returns to the event loop by returning 'true'
		DeferredCommand.addCommand(ic);
	}
	
	/**
	 * This is where the EventView is created
	 * @param eventModel
	 */
	private void buildEventView(EventModel eventModel) {
		// Create an EventView object from the EventModel
		EventView eventView = new EventView(eventModel);
		EventResizeListener listener = new EventResizeListener(getEventViews(), model);
		listener.registerPositioner(positioner);
		eventView.addListener(listener);
		eventViews.add(eventView);
		addEvent(eventView);
		
		// Get the top and bottom relative position of the desired location
		//TODO first: check if this is taking too much time
		// then: if this is taking some time it could be calculated for each location and stored somewhere...
		// this means update the values whenever a location is added... ok it would be hard.
		int[] bounds = WidgetPositioner.findLocationBounds(eventModel.getLocation(),
				model.getLocationHeights(),
				model.getLineHeight() );
		int topLinePosition = bounds[0];

		int leftPosition = positioner.getLeftPosition(eventView);
		int width = positioner.getEventWidth(eventView);
		eventView.setWidth(width);
		eventView.setHeight(model.getLineHeight());
		moveEvent(eventView,leftPosition,topLinePosition);
		positioner.resolveOverlapping(eventView, eventViews);
		
		//TODO this is actually unneeded and is only a bug fix. Find the bug! (Somewhere, there is an update to the class attribute that should not be there. This bug happens only when initialising and wasn't there before rev16(?).
		eventView.updateVisualStyle();
	}
	
	
	/**
	 * Removes all events from the absolute panel, and clear their reference
	 */
	public void clearEvents() {
		for (EventView event : getEventViews()) {
//			targetPanel.remove(event);
			event.removeFromParent();
		}
		eventViews.clear();
	}
	
	/**
	 * Call this in case of requestAddLine(step, location)
	 * Performance function: this could be done by a clearEvents() + updateEvents()
	 * Besides, there's a weird duplication of the first event with the above method
	 * @param location
	 * @param step
	 */
	public void pushEventsFromLocation(Location location, int step) {
		int leftRelativeDif = getEventRelativeLeftDifference();
		int topRelativeDif = getEventRelativeTopDifference();
		
		while(!Location.next().equals(location));
		while (Location.hasNext()) {
			Location loc = Location.next();
			
			for (EventView event : getEventViews()) {
				
				if (event.getModel().getLocation().equals(loc))
					moveEvent(event, event.getAbsoluteLeft()-leftRelativeDif,
							event.getAbsoluteTop()-topRelativeDif+step*model.getLineHeight());
			}
		}
	}

	public void addEvent(EventView event) {
		// add it to the DOM so that offset width/height becomes available
		targetPanel.add(event, 0, 0);
		
		// can't set z-index before render
		//TODO commenting this line doesn't change a thing... ??? there must be another instance changing the DOM
//		event.getElement().setAttribute("style",
//				event.getElement().getAttribute("style")+"z-index:7;");

		// make the label draggable
		dragController.makeDraggable(event, event.dragHandle);
	}
	
	/**
	 * Move the event in the target panel
	 * Caution: the event must be rendered and attached to the target panel
	 * @param event
	 * @param left, position relative to the target panel
	 * @param top, position relative to the target panel
	 */
	public void moveEvent (EventView event, int left, int top) {
		targetPanel.setWidgetPosition(event, left, top);
	}
	
	
	public int getEventRelativeTopDifference() {
		EventView event = EventFactory.createRandomEventView();
		targetPanel.add(event, 0, 0);
		int top = event.getAbsoluteTop();
		targetPanel.remove(event);
		return top;
	}
	public int getEventRelativeLeftDifference() {
		EventView event = EventFactory.createRandomEventView();
		targetPanel.add(event, 0, 0);
		int left = event.getAbsoluteLeft();
		targetPanel.remove(event);
//		com.extjs.gxt.ui.client.widget.Info.display("referential",
//				"left difference: "+left);	
		return left;
	}
	
	//------------------------------------------------------------------------------------
	// general update
	
	
	/**
	 * Updates everything
	 */
	public void update() {
		updateLocationGridDimensions();
		updateTimeGridDimensions();
		updateBoundaryDimensions();
		updateEvents();
	}
	
	/**
	 * Updates everything except location grid
	 */
	public void updateTimeEvents() {
		timeSpaceRuler.update();
		updateEvents();
	}
	
}
