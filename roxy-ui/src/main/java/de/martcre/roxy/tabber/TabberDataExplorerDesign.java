package de.martcre.roxy.tabber;


import org.apache.metamodel.data.Row;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class TabberDataExplorerDesign extends VerticalLayout {
	
	private Grid<Row> grid;
	private CheckBox liveFilteringMode;
	private CheckBox enableAutomaticWildcards;
	
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
	
	/**
	 * The setting for the Live Filtering Mode.
	 * 
	 * @return the checkbox
	 */
	public CheckBox getLiveFilteringMode() {
		return liveFilteringMode;
	}
	
	/**
	 * The setting for enabling Wildcards.
	 * 
	 * @return the checkbox
	 */
	public CheckBox getEnableAutomaticWildcards() {
		return enableAutomaticWildcards;
	}

}
