package de.martcre.roxy.tabber;

import org.apache.metamodel.data.Row;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class TabberDataExplorerDesign extends VerticalLayout {
	
	private Grid<Row> grid;
	
	public TabberDataExplorerDesign() {
		Design.read(this);
	}
	
	/**
	 * The main grid
	 * 
	 * @return the main grid
	 */
	public Grid<Row> getGrid() {
		return grid;
	}

}
