package de.martcre.roxy.tabber;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class TabberMainDesign extends VerticalLayout {

	private VerticalLayout viewport;
	
	public TabberMainDesign() {
		Design.read(this);
	}
	
	/**
	 * Get the main viewport of tabber.
	 * 
	 * @return the viewport
	 */
	public VerticalLayout getViewport() {
		return viewport;
	}
	
}
