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

import com.eSOS.eventViewer.client.view.EventView;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.DragContext;

/**
 * A {@link DropController} which constrains the placement of draggable widgets
 * the grid specified in the constructor.
 */
public class GridConstrainedDropController extends
AbsolutePositionDropController implements HasPositioner {
	private int gridX;

	private int gridY;

	private WidgetPositioner<EventView> positioner =
		new WidgetPositioner<EventView>(null,null);
	/**
	 * This boolean permits to check if the WidgetPositionner is registered
	 * (debug purpose)
	 */
	private boolean hasPositioner = false;

	public GridConstrainedDropController(AbsolutePanel dropTarget, int gridX,
			int gridY) {
		super(dropTarget);
		this.gridX = gridX;
		this.gridY = gridY;
	}

	public void changeGrid(int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
	}

	@Override
	public void drop(Widget widget, int left, int top) {
		left = Math.max(
				0,
				Math.min(left,
						dropTarget.getOffsetWidth() - widget.getOffsetWidth()));
		top = Math.max(0, Math.min(top,
				dropTarget.getOffsetHeight() - widget.getOffsetHeight()));
		left = Math.round((float) left / gridX) * gridX;
		top = Math.round((float) top / gridY) * gridY;
		dropTarget.add(widget, left, top);
	}

	@Override
	public void onDrop(DragContext context) {
		for (Draggable draggable : draggableList) {

			// TODO here we have the info for the old position
			//			com.extjs.gxt.ui.client.widget.Info.display("Positioner left", ""
			//					+ draggable.positioner.getAbsoluteLeft());
			//			com.extjs.gxt.ui.client.widget.Info.display("Positioner top", ""
			//					+ draggable.positioner.getAbsoluteTop());

			// before placing the event we must check for possible overlap
			if(hasPositioner())
				positioner.resolveDropOverlapping(draggable);

			draggable.positioner.removeFromParent();
			dropTarget.add(draggable.widget, draggable.desiredX,
					draggable.desiredY);


			// TODO here we have the info for the new position
			//			com.extjs.gxt.ui.client.widget.Info.display("DesiredX", ""
			//					+ draggable.desiredX);
			//			com.extjs.gxt.ui.client.widget.Info.display("DesiredY", ""
			//					+ draggable.desiredY);
		}
		super.onDrop(context);
	}

	@Override
	public void onMove(DragContext context) {
		super.onMove(context);
		for (Draggable draggable : draggableList) {
			draggable.desiredX = context.desiredDraggableX - dropTargetOffsetX
			+ draggable.relativeX;
			draggable.desiredY = context.desiredDraggableY - dropTargetOffsetY
			+ draggable.relativeY;
			draggable.desiredX = Math.max(
					0,
					Math.min(draggable.desiredX, dropTargetClientWidth
							- draggable.offsetWidth));
			draggable.desiredY = Math.max(
					0,
					Math.min(draggable.desiredY, dropTargetClientHeight
							- draggable.offsetHeight));
			draggable.desiredX = Math.round((float) draggable.desiredX / gridX)
			* gridX;
			draggable.desiredY = Math.round((float) draggable.desiredY / gridY)
			* gridY;
			dropTarget.add(draggable.positioner, draggable.desiredX,
					draggable.desiredY);
		}
	}

	public void registerPositioner(WidgetPositioner<EventView> positioner) {
		this.positioner = positioner;
		this.hasPositioner = true;
	}
	public void removePositioner() {
		this.hasPositioner = false;
	}

	public boolean hasPositioner() {
		return this.hasPositioner;
	}
}
