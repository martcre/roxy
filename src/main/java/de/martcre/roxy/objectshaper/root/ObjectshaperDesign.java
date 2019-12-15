package de.martcre.roxy.objectshaper.root;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class ObjectshaperDesign extends VerticalLayout {
	
	private VerticalLayout viewport;
	private MenuBar menu;
	private MenuBar infoBar;
	
	public ObjectshaperDesign() {
		Design.read(this);
		
		MenuItem file = menu.addItem("File");
		file.addItem("New");
		file.addItem("Open File");
		file.addItem("Recent Files");
		file.addSeparator();
		file.addItem("Switch Workspace");
		
	}
	
	public VerticalLayout getViewport() {
		return viewport;
	}

}
