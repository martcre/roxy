package de.martcre.roxy.desktop;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class WrapperManagerDesign extends HorizontalLayout {

	
	public WrapperManagerDesign() {
		Design.read(this);
	}
	
}
