package de.martcre.roxy.desktop;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class DockedWrapperDesign extends VerticalLayout {

	private Label placeholder;
	private Label panelCaption;
	private Button undockButton;

	public DockedWrapperDesign() {
		Design.read(this);
	}
	
	public Label getPlaceholder() {
		return placeholder;
	}
	
	public Button getUndockButton() {
		return undockButton;
	}
	
	public Label getPanelCaption() {
		return panelCaption;
	}
}
