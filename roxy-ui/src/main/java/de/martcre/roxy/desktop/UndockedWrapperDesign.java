package de.martcre.roxy.desktop;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class UndockedWrapperDesign extends VerticalLayout {

	private Label placeholder;
	
	public UndockedWrapperDesign() {
		Design.read(this);
	}
	
	public Label getPlaceholder() {
		return placeholder;
	}
}
