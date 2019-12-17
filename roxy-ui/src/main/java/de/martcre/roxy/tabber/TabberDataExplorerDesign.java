package de.martcre.roxy.tabber;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class TabberDataExplorerDesign extends VerticalLayout {
	
	private Grid grid;
	
	public TabberDataExplorerDesign() {
		Design.read(this);
	}
	
	/**
	 * Get the main grid.
	 * 
	 * @return the grid
	 */
	public Grid getGrid() {
		return grid;
	}

}
