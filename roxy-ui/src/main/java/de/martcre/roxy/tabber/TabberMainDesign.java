package de.martcre.roxy.tabber;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

import de.martcre.roxy.desktop.WrapperManager;

@SuppressWarnings("serial")
@DesignRoot
public class TabberMainDesign extends VerticalLayout {

	private VerticalLayout viewport;
	private WrapperManager wrapperManager;
	
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
	
	/**
	 * Get the Wrapper Manager.
	 * 
	 * @return the Wrapper Manager
	 */
	public WrapperManager getWrapperManager() {
		return wrapperManager;
	}
	
}
